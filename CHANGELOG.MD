# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [1.3.1] - 2018-06-05
### Changed
- Mise à jours des versions des librairies
    * spring 1.5.6 -> 1.5.9
    * logback-encoder 4.7 -> 5.1
- Ajout de deux threads mdc pour améliorer le diagnostic ('threadMDC1', 'threadMDC2')
- Changement de la façon d'ajouter les appenders en fonction des profils spring

## [1.3.0] - 2018-03-15
### Changed
- Activation de l'autoconfiguration pour le bean LogMetierService

## [1.2.1] - 2018-02-01
### Changed
- Fix bug in _log-metiers.xml_ when profile is 'dev'
- Ajout tests unitaires
- Correction sonar

## [1.2.0] - 2017-09-14
### Changed
- Only log on file when profile is not 'dev'

## [1.1.1] - 2017-09-08
### Changed
- Fix bug and replace FileAppender by RollingFileAppender

## [1.1.0] - 2017-09-08
### Added
- define environment variable MAX_HISTORY
- define environment variable MAX_FILE_SIZE
- define environment variable TOTAL_SIZE_CAP
- Add log rotation configuration in logs-defaults.xml with SizeAndTimeBasedRollingPolicy
- Add log rotation configuration in logs-metiers.xml with SizeAndTimeBasedRollingPolicy

### Changed
- Rename environment variable ENV_VAR_LOG_FILE to LOG_FILE
- Rename environment variable ENV_VAR_METIER_LOG_FILE to LOG_FILE_JSON

## [1.0.0] - 2017-09-07
### Added
- logs-defaults.xml configuration
- logs-metiers.xml configuration
- define environment variable ENV_VAR_LOG_FILE
- define environment variable ENV_VAR_METIER_LOG_FILE
- Add LogMetierService component

