#!/bin/bash -xe

export TCK_HOME=${WORKSPACE}
echo "TCK_HOME in jaxrtck.sh $TCK_HOME"
echo "ANT_HOME in jaxrtck.sh $ANT_HOME"
echo "PATH in jaxrtck.sh $PATH"
echo "ANT_OPTS in jaxrtck.sh $ANT_OPTS"

if [ ! -z "$JAXRTCK_BUNDLE_BASE_URL" ]; then
  mkdir -p ${WORKSPACE}/standalone-bundles/
  wget  --progress=bar:force --no-cache ${JAXRTCK_BUNDLE_BASE_URL}/$JAXRTCK_BUNDLE_FILE_NAME -O ${WORKSPACE}/standalone-bundles/$JAXRTCK_BUNDLE_FILE_NAME
fi

### Registry server initialization starts here
cd /opt/jwsdp-1.3/bin
./startup.sh
sleep 10
echo "Java Web Services Developer Pack started ..."
### Registry server initialization ends here


cd $TCK_HOME
if ls ${WORKSPACE}/standalone-bundles/*jaxrtck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for jaxrtck created during the build phase"
  unzip ${WORKSPACE}/standalone-bundles/*jaxrtck*.zip -d ${TCK_HOME}
  TCK_NAME=jaxrtck
elif ls ${WORKSPACE}/standalone-bundles/*xml-registries-tck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for xml-registries-tck created during the build phase"
  unzip ${WORKSPACE}/standalone-bundles/*xml-registries-tck*.zip -d ${TCK_HOME}
  TCK_NAME=xml-registries-tck
else
  echo "[ERROR] TCK bundle not found"
  exit 1
fi

##### installRI.sh starts here #####
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "${GF_BUNDLE_URL}" ]; then
  echo "[ERROR] GF_BUNDLE_URL not set"
  exit 1
fi
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
unzip ${TCK_HOME}/latest-glassfish.zip -d ${TCK_HOME}

TS_HOME=$TCK_HOME/$TCK_NAME
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME
cd $TS_HOME/bin

sed -i "s#^registryURL =.*#registryURL=http://localhost:8080/RegistryServer/#g" ts.jte
sed -i "s#^queryManagerURL =.*#queryManagerURL=http://localhost:8080/RegistryServer/#g" ts.jte
sed -i "s#^jaxrUser=.*#jaxrUser=testuser#g" ts.jte
sed -i "s#^jaxrPassword=.*#jaxrPassword=testuser#g" ts.jte
sed -i "s#^jaxrUser2=.*#jaxrUser2=jaxr-sqe#g" ts.jte
sed -i "s#^jaxrPassword2=.*#jaxrPassword2=jaxrsqe#g" ts.jte
sed -i "s#^ts_home=.*#ts_home=$TS_HOME/#g" ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME}#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME}#g" ts.jte

sed -i "s#^jwsdp\.home=.*#jwsdp.home=$TCK_HOME/glassfish5/glassfish/#g" $TS_HOME/bin/build.properties
if [ ! -z "$TCK_BUNDLE_BASE_URL" ]; then
  sed -i 's/javax.xml.registry-api.jar/jakarta.xml.registry-api.jar/g' $TS_HOME/bin/build.properties
  sed -i 's/javax.xml.bind-api.jar/jakarta.xml.bind-api.jar/g' $TS_HOME/bin/build.properties
fi

echo "ts.home=$TS_HOME" >>$TS_HOME/bin/build.properties

GF_HOME=$TCK_HOME/glassfish5/glassfish/

mkdir -p $TCK_HOME/${TCK_NAME}report/${TCK_NAME}
mkdir -p $TCK_HOME/${TCK_NAME}work/${TCK_NAME}-sig
mkdir -p $TCK_HOME/${TCK_NAME}report/${TCK_NAME}-sig
mkdir -p $TCK_HOME/${TCK_NAME}work/${TCK_NAME}-sig

cd $TS_HOME/src/com/sun/ts/tests/jaxr/
ant -Dts.home=$TS_HOME -Djwsdp.home=$TCK_HOME/glassfish5/glassfish -propertyfile $TS_HOME/bin/build.properties -Dreport.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME} -Dwork.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME} runclient
cd $TS_HOME/src/com/sun/ts/tests/signaturetest/jaxr/
ant -Dts.home=$TS_HOME -Djwsdp.home=$TCK_HOME/glassfish5/glassfish -propertyfile $TS_HOME/bin/build.properties -Dreport.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME}-sig -Dwork.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME}-sig runclient
echo "Test run complete"

