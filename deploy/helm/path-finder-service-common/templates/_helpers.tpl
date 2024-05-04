{{- /*
path-finder-service-common.util.merge will merge two YAML templates and output the result.
This takes an array of three values:
- the top context
- the template name of the overrides (destination)
- the template name of the base (source)
*/}}
{{- define "path-finder-service-common.util.merge" -}}
{{- $top := first . -}}
{{- $overrides := fromYaml (include (index . 1) $top) | default (dict ) -}}
{{- $tpl := fromYaml (include (index . 2) $top) | default (dict ) -}}
{{- toYaml (merge $overrides $tpl) -}}
{{- end -}}

{{- define "path-finder-service-common.waitForPostgresql"}}
name: wait-for-postgresql
image: busybox:latest
imagePullPolicy: IfNotPresent
command: [ 'sh', '-c', 'until nc -vz ${POSTGRESQL_URI}; do echo "Waiting for postgresql..."; sleep 3; done;' ]
env:
  - name: POSTGRESQL_URI
    valueFrom:
      configMapKeyRef:
        name: {{ include "path-finder-service-common.fullnameGenerator" (dict "chartName" "path-finder-postgresql-public" "Values" .Values "Release" .Release) }}
        key: URI
{{- end -}}

{{- define "path-finder-service-common.waitForNeo4j"}}
name: wait-for-neo4j
image: busybox:latest
imagePullPolicy: IfNotPresent
command: [ 'sh', '-c', 'until nc -vz ${NEO4J_URI}; do echo "Waiting for neo4j..."; sleep 3; done;' ]
env:
  - name: NEO4J_URI
    valueFrom:
      configMapKeyRef:
        name: {{ include "path-finder-service-common.fullnameGenerator" (dict "chartName" "path-finder-neo4j-public" "Values" .Values "Release" .Release) }}
        key: URI
{{- end -}}

{{/*
Expand the name of the chart.
*/}}
{{- define "path-finder-service-common.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "path-finder-service-common.fullname" -}}
{{ include "path-finder-service-common.fullnameGenerator" (dict "chartName" .Chart.Name "Values" .Values "Release" .Release) }}
{{- end }}

{{- define "path-finder-service-common.fullnameGenerator" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .chartName .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
envFrom for the Deployment.
*/}}
{{- define "path-finder-service-common.envFrom" -}}
{{- if .Values.includeChartConfig }}
- configMapRef:
    name: {{ include "path-finder-service-common.fullname" . }}
{{- end }}
{{- if .Values.includeChartSecret }}
- secretRef:
    name: {{ include "path-finder-service-common.fullname" . }}
{{- end }}
{{- if .Values.envFrom.extraClusterSecret }}
{{- range .Values.envFrom.extraClusterSecret }}
- secretRef:
    name: {{ include "path-finder-service-common.fullnameGenerator" (dict "chartName" .chartName "Values" $.Values "Release" $.Release) }}
{{- end }}
{{- end }}
{{- if .Values.envFrom.extraClusterConfigmap }}
{{ range .Values.envFrom.extraClusterConfigmap }}
- configMapRef:
    name: {{ include "path-finder-service-common.fullnameGenerator" (dict "chartName" .chartName "Values" $.Values "Release" $.Release) }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "path-finder-service-common.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "path-finder-service-common.labels" -}}
helm.sh/chart: {{ include "path-finder-service-common.chart" . }}
{{ include "path-finder-service-common.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "path-finder-service-common.selectorLabels" -}}
app.kubernetes.io/name: {{ include "path-finder-service-common.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}
