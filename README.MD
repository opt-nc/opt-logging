# opt-logging
La librairie contient les 2 fichiers de configuration de logback préconisé pour les développements d'application à l'OPT.

## Build gradle
Ajouter la dépendance suivante dans votre build.gradle :

```gradle
compile group: 'nc.opt.core', name: 'opt-logging', version: '1.0.0'
```

## Configuration du logback

### Inclusion de la configuration par défaut

```logback
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true">
	<property name="ENV_VAR_LOG_FILE" value="${ENV_VAR_LOG_FILE}"/>
	<include resource="nc/opt/core/logging/logs-defaults.xml" />
</configuration>
```

### Inclusion de la configuration pour les logs métiers
```logback
<property name="ENV_VAR_METIER_LOG_FILE" value="${ENV_VAR_METIER_LOG_FILE}"/>
<include resource="nc/opt/core/logging/logs-metiers.xml" />
```

## Fonctionnalités
Dans la configuration par défaut en version 1.0.0 :
<ul>
<li>Il n'y a pas de rotation des logs car elle est assurée par le SEP.</li>
<li>Avec le profile dev ou DEV les logs s'affichent dans la console en plus d'être dans le fichiers</li>
<li>Le rootLogger est par défaut à INFO</li>


## Variables d'environnement
2 variables d'environnement sont nécessaires : 
ENV_VAR_LOG_FILE qui contient le path vers le fichier des logs classiques
Les logs classiques sont les fichiers de logs java de base.

ENV_VAR_METIER_LOG_FILE qui contient le path vers le fichier des logs métiers
Les logs métiers servent à logguer des objest directement en json.

## Utilisation du service de log métier

### Déclaration
```java
@Autowired
private LogMetierService logMetierService;
```

### Utilisation
```java
logMetierService.logObject("pays", new Pays());
```

