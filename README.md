# Xenon Command Line Interface

[![Build Status](https://travis-ci.org/NLeSC/xenon-cli.svg?branch=master)](https://travis-ci.org/NLeSC/xenon-cli)
[![Build status](https://ci.appveyor.com/api/projects/status/vki0xma8y7glpt09/branch/master?svg=true)](https://ci.appveyor.com/project/NLeSC/xenon-cli/branch/master)
[![SonarQube Gate](https://sonarqube.com/api/badges/gate?key=nlesc%3Axenon-cli)](https://sonarqube.com/dashboard?id=nlesc%3Axenon-cli)
[![SonarQube Coverage](https://sonarqube.com/api/badges/measure?key=nlesc%3Axenon-cli&metric=coverage)](https://sonarqube.com/component_measures/domain/Coverage?id=nlesc%3Axenon-cli)
[![DOI](https://zenodo.org/badge/80642209.svg)](https://zenodo.org/badge/latestdoi/80642209)

Command line interface which uses the [Xenon library](https://nlesc.github.io/Xenon) to perform job and file operations.

# Install

Goto https://github.com/NLeSC/xenon-cli/releases and download a tarball (or zipfile).
The tarball can be installed with:
```
tar -xf build/distributions/xenon*.tar
xenon*/bin/xenon --help
```
Add `xenon*/bin` to your PATH environment variable for easy usage.

# Usage

```
xenon filesystem file list /etc
xenon filesystem sftp --location localhost list /etc
xenon filesystem sftp --location localhost upload /etc/passwd /tmp/copy-of-passwd
xenon scheduler ssh --location localhost exec /bin/hostname
echo "sleep 30;echo Hello" | xenon sftp --location localhost upload - /tmp/myjob.sh
xenon scheduler slurm --location localhost submit /bin/sh /tmp/myjob.sh
```

To keep password or passphrase invisible in process list put the password in a text file (eg. 'password.txt') and then use '@password.txt' as argument.
For example:
```
xenon filesystem sftp --location localhost --username $USER --password @password.txt list $PWD/src
```

# Build

```
./gradlew build
```

Generates application tar/zip in `build/distributions/` directory.

# Release

1. Create a new GitHub release
2. Upload the files in `build/distributions/` directory to that release
3. Publish release

## Docker

Run Xenon CLI as a Docker container.

The Docker image can be build with
```
./gradlew docker
```

Generates a `nlesc/xenon-cli` Docker image.

To use local files use volume mounting (watch out as the path should be relative to mount point):
```
docker run -ti --rm nlesc/xenon-cli --user $USER -v $PWD:/work --adaptor ssh upload --source /work/somefile.txt --location localhost --path /tmp/copy-of-somefile.txt 
```

## Common Workflow Language

Run Xenon CLI using a cwl-runner or as a tool in a Common Workflow Language workflow.

Requires `nlesc/xenon-cli` Docker image to be available locally.

Example to list contents of `/etc` directory via a ssh to localhost connection with cwl-runner:
```
./xenon-ls.cwl --scheme sftp --location $USER@172.17.0.1 --path $PWD --certfile ~/.ssh/id_rsa
./xenon-upload.cwl --certfile ~/.ssh/id_rsa --scheme sftp --location $USER@172.17.0.1 --source README.md --target $PWD/copy-of-README.md
./xenon-download.cwl --certfile ~/.ssh/id_rsa --scheme sftp --location $USER@172.17.0.1 --source $PWD/README.md --target copy-of-README.md
```
(Replace `<user>@<host>` with actual username and hostname + expects docker with default network range)
