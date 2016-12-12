#!/bin/sh
#
# Tool to maintain p2 repositories
# https://wiki.eclipse.org/Equinox/p2/Publisher

if [ -z "$RUNTIME_DIR" ]; then
    echo "No RUNTIME_DIR set. Using default '/Applications/Eclipse.app/Contents/Eclipse'."
    export RUNTIME_DIR=/Applications/Eclipse.app/Contents/Eclipse
fi

mode=
repoDir=
repoName=

usage() {
  echo "Usage:"
  echo "  $0"
}

fail() {
  echo P2 Repository Tool
  if [ $# -gt 0 ]; then
    echo "Error: $1"
  fi
  usage
  exit 1
}

if [ -z "$RUNTIME_DIR" ]; then
  fail "Missing RUNTIME_DIR, must point to an Eclipse installation"
fi

if [ ! -d "$RUNTIME_DIR/plugins" ]; then
  fail "Invalid RUNTIME_DIR: $RUNTIME_DIR, must point to an Eclipse installation"
fi

# Find Equinox launcher
launcher=$RUNTIME_DIR/plugins/`ls -1 $RUNTIME_DIR/plugins 2> /dev/null | grep launcher_ | tail -n 1`
echo "Using Equinox launcher: $launcher"

mkdir -p /tmp/repository/plugins
mkdir -p /tmp/repository/features

cp plugins/* /tmp/repository/plugins
cp feature.xml /tmp/repository/features

java -jar $launcher \
  -application org.eclipse.equinox.p2.publisher.FeaturesAndBundlesPublisher \
  -metadataRepository file:$(pwd)/target/ \
  -artifactRepository file:$(pwd)/target/ \
  -source /tmp/repository \
  -configs gtk.linux.x86 \
  -compress \
  -publishArtifacts \
  || fail
