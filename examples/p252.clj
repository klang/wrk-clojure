(def db {:classname "org.hsqldb.jdbcDriver"
	 :subprotocol "hsqldb"
	 :subname "file:/home/klang/wrk-clojure/exampels/p252.db"
	 })
;	 :subname "c:/klang/wrk-clojure/examples/p252.db"
;	 :subname "file:c:/klang/wrk-clojure/examples/p252.db"
;	 :subname "file:c:\\klang\\wrk-clojure\\examples\\p252.db"

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

(defn now [] (java.sql.Timestamp. (.getTime (java.util.Date.))))

(defn insert-snippets []
  (let [timestamp (now)]
    (seq
     (insert-values :snippets
		    [:body :created_at]
		    ["(println :boo)" timestamp]
		    ["(defn foo [] 1)" timestamp]))))

(with-connection db (insert-snippets))

(defn print-snippets []
  (with-query-results res ["select * from snippets"]
    (println res)))

(with-connection db (print-snippets))

(defn select-snippets []
  (with-connection db
    (with-query-results res ["select * from snippets"] (doall res))))

(with-connection db (select-snippets))

(defn sql-query [q]
  (with-query-results res q (doall res)))

(with-connection db (sql-query ["select body from snippets"]))

(defn last-created-id
  "Extract the last created id. Must be called in a transaction
   that performed an insert. Expects HSQLDB return structure of
   the form [{:@p0 id}]."
  []
  (first (vals (first (sql-query ["CALL IDENTITY()"])))))

(defn insert-snippet [body]
  (with-connection db
    (transaction
      (insert-values :snippets
		     [:body :created_at]
		     [body (now)])
      (last-created-id))))

(insert-snippet "(+ 1 1)")
(insert-snippet "(ref true)")