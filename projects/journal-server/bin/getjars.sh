echo "getting jars, remove them back by running ./rmjars.sh"
BASE=~/wrk-clojure/tutorials/programming-clojure
LIB=$BASE/lib
PUB=$BASE/public
mkdir -p ./lib ./public
cp $LIB/commons-io-1.4.jar $LIB/commons-fileupload-1.2.1.jar $LIB/commons-codec-1.3.jar $LIB/clojure.jar $LIB/clojure-contrib.jar $LIB/compojure.jar $LIB/hsqldb.jar $LIB/jetty-6.1.14.jar $LIB/jetty-util-6.1.14.jar $LIB/servlet-api-2.5-6.1.14.jar ./lib
cp -R $PUB/* ./public