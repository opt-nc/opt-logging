# opt-logging
La librairie contient les 2 fichiers de configuration de logback préconisé pour les développements d'application à l'OPT.

## Build gradle
Ajouter la dépendance suivante dans votre build.gradle :

```gradle
compile group: 'nc.opt.core', name: 'opt-logging', version: '1.2.0'
```

## Configuration du logback

### Inclusion de la configuration par défaut

```logback
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true">
	<property name="LOG_FILE" value="${LOG_FILE}"/>
	<property name="MAX_HISTORY" value="${MAX_HISTORY}"/>
	<property name="MAX_FILE_SIZE" value="${MAX_FILE_SIZE}"/>
	<property name="TOTAL_SIZE_CAP" value="${TOTAL_SIZE_CAP}"/>
	<include resource="nc/opt/core/logging/logs-defaults.xml" />
</configuration>
```

### Inclusion de la configuration pour les logs métiers
```logback
<property name="LOG_FILE_JSON" value="${LOG_FILE_JSON}"/>
<include resource="nc/opt/core/logging/logs-metiers.xml" />
```

## Fonctionnalités
Dans la configuration par défaut en version 1.2.0 :
<ul>
<li>La rotation de logs est configurée dans les fichier de la librairie. on utilise [SizeAndTimeBasedRollingPolicy](https://logback.qos.ch/manual/appenders.html).</li>
<li>Avec le profil 'dev' les logs s'affichent uniquement dans la console, les variables d'environnement sont donc facultatives</li>
<li>Avec un profil différent de 'dev' les logs s'affichent uniquement dans les fichiers et il faut définir les variables d'environnement</li>
<li>Le rootLogger est par défaut à INFO</li>
</ul>

## Variables d'environnement
5 variables d'environnement sont nécessaires avec un profil non dev : 
<ul>
<li>LOG_FILE : contient le path vers le fichier des logs classiques.
Les logs classiques sont les fichiers de logs java de base.
la variable doit se terminer par .log</li>
<li>LOG_FILE_JSON : contient le path vers le fichier des logs métiers
Les logs métiers servent à logguer des objets directement en json.
la variable doit se terminer par .json</li>
<li>MAX_HISTORY : Nombre de fichiers max à garder pour historique. A savoir que la rotation est effectuée par jour et par taille.</li>
<li>MAX_FILE_SIZE : Taille max d'un fichier pour la rotation. ex : 100MB.</li>
<li>TOTAL_SIZE_CAP : Taille max de la somme des fichiers présents. ex : 5GB</li>
</ul>

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

