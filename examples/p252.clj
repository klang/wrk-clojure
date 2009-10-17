(def db {:classname "org.hsqldb.jdbcDriver"
	 :subproticol "hsqldb"
	 :subname "c:/klang/wrk-clojure/examples/p252.db"
;	 :subname "file:c:/klang/wrk-clojure/examples/p252.db"
;	 :subname "file:c:\\klang\\wrk-clojure\\examples\\p252.db"
	 })

(use 'clojure.contrib.sql)

(defn create-snippets []
  (create-table :snippets
		[:id :int "IDENTITY" "PRIMARY KEY"]
		[:body :varchar "NOT NULL"]
		[:created_at :datetime]))

;; (create-snippets)
;; no current database connection
;;   [Thrown class java.lang.Exception]

(with-connection db (create-snippets))
;;db-spec {:classname "org.hsqldb.jdbcDriver", :subproticol "hsqldb", 
;;:subname "file:~/wrk-clojure/examples/p252.db"} is missing a required parameter
;;  [Thrown class java.lang.IllegalArgumentException]

