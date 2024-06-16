#!/bin/bash
set -e  # Exit immediately if a command exits with a non-zero status

# Function to display help
show_help() {
    echo "Usage: $0 [OPTION]"
    echo "Options:"
    echo "  --destroy    Destroy minikube (Windows only)"
    echo "  --plan       Run only terraform plan"
    echo "  -h, --help   Display this help message"
}

# Default values
DESTROY_PARAM=""
PLAN_PARAM=""
SHOW_HELP=0

# Parse arguments
for arg in "$@"; do
    case $arg in
        --destroy) DESTROY_PARAM="--destroy" ;;
        --plan) PLAN_PARAM="--plan" ;;
        -h|--help) SHOW_HELP=1 ;;
        *) echo "Unknown option: $arg"; SHOW_HELP=1 ;;
    esac
done

# Display help if needed
[ $SHOW_HELP -eq 1 ] && { show_help; exit 0; }

run_as_admin() {
    powershell -Command "Start-Process cmd -ArgumentList '/c $1' -Verb RunAs -Wait"
}

check_os() {
    case "$(uname -s)" in
        Darwin) echo "macOS" ;;
        CYGWIN*|MINGW*|MSYS*) echo "Windows" ;;
        *) echo "unknown" ;;
    esac
}

is_minikube_running() {
    current_context=$(kubectl config current-context 2>/dev/null)
    if [ -z "$current_context" ]; then
        return 1
    elif echo "$current_context" | grep -q "minikube"; then
        if kubectl get nodes &>/dev/null; then
            return 0
        else
            return 1
        fi
    else
        return 1
    fi
}

OS=$(check_os)

if [ "$DESTROY_PARAM" == "--destroy" ]; then
    if [ "$OS" == "Windows" ]; then
        cd terraform && terraform destroy -var "github_username=" -var "github_pat=" && cd ..
        run_as_admin "minikube delete"
        exit 10
    else
        echo "--destroy option is only supported on Windows"
        exit 1
    fi
fi

if ! is_minikube_running; then
    case "$OS" in
        macOS)
            brew install minikube kubectl git gh
            minikube start
            ;;
        Windows)
            run_as_admin "choco install minikube kubernetes-cli git gh -y && minikube start"
            powershell -Command "Start-Process powershell -ArgumentList '-NoProfile -ExecutionPolicy Bypass -Command \"minikube tunnel\"' -Verb RunAs"
            ;;
        *)
            echo "Unsupported operating system"
            exit 1
            ;;
    esac
else
    echo "Minikube is already running. Skipping setup of minikube."
fi

GITHUB_USERNAME=$(gh api user | jq -r '.login')
GITHUB_PAT=$(gh auth token)

if [ -z "$GITHUB_PAT" ]; then
    echo "Error: GitHub Personal Access Token (GITHUB_PAT) is not set."
    exit 1
fi

# build and upload image
# Authenticate with GitHub CLI if not already authenticated
gh auth status > /dev/null 2>&1 || gh auth login

# Get Kubernetes node IP address
K8S_IP=$(kubectl get nodes -o=jsonpath='{.items[0].status.addresses[?(@.type=="InternalIP")].address}')

# Execute Maven build with Jib plugin, passing the Kubernetes IP as a property
mvn compile jib:build -Dimage.registry.ip="$K8S_IP" \
                      -Djib.to.auth.username="$GITHUB_USERNAME" \
                      -Djib.to.auth.password="$GITHUB_PAT"

# minikube deploy
dockerconfigjson=$(echo -n "{\"auths\":{\"ghcr.io\":{\"auth\":\"$(echo -n "$GITHUB_USERNAME:$GITHUB_PAT" | base64)\"}}}" | base64)
echo $dockerconfigjson

cd terraform
terraform init
terraform plan -var "github_username=$GITHUB_USERNAME" -var "github_pat=$GITHUB_PAT"

if [ "$PLAN_PARAM" == "--plan" ]; then
    exit 10
fi

terraform apply -auto-approve -var "github_username=$GITHUB_USERNAME" -var "github_pat=$GITHUB_PAT"

kubectl port-forward -n paf svc/interview-exercise-service 8080:8080 &
kubectl port-forward -n monitoring svc/kube-prometheus-stack-grafana 80:80 &
kubectl port-forward -n monitoring svc/kube-prometheus-stack-prometheus 9090:9090 &
kubectl port-forward -n paf svc/postgresql 5432:5432 &

echo "Grafana password: $(kubectl get secret --namespace monitoring kube-prometheus-stack-grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo)"
