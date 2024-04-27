# How can I run the project using Minikube?

1. Run a Minikube cluster, ex: `minikube start --memory=4096 --cpus=4`
2. Update the Helm dependencies. As for now, Helm does not support recursive updates of the dependencies. To bypass this limitation, one can use the
   following bash script: `sudo ./helm-update-recursive.sh ./path-finder`
3. Install the Helm chart: `helm install -f VALUES.yml RELEASE_NAME ./path-finder`