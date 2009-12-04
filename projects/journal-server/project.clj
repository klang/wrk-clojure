(defproject journal-server "0.1"
  :description "erik lavigne's journal-server with authorization but using hsqldb"
  :url "http://github.com/klang/wrk-clojure/tree/master/projects/journal-server/"
  :dependencies [[org.clojure/clojure "1.1.0-alpha-SNAPSHOT"]
                 [org.clojure/clojure-contrib "1.0-SNAPSHOT"]
		 [org.clojars.ato/compojure "0.3.1"]
		 [org.hsqldb/hsqldb "1.8.0.10"]]
  :dev-dependencies [[lein-clojars "0.5.0-SNAPSHOT"]
		     [org.clojure/swank-clojure "1.0"]])

