(ns article
  (:require [clojure.contrib.sql :as sql]))

(comment 
(defn create-article []
  (sql/create-table :article
		[:id           :serial "primary key"]
		[:title        :text "NOT NULL" "unique check (trim(title) '')"]
		[:description  :text "NOT NULL" "default ''"]
		[:body         :text "NOT NULL" "default ''"]
		[:created      :timestamp "NOT NULL" "default now()"]
		[:updated      :timestamp "NOT NULL" "default now()"]
		[:published    :timestamp "NULL" "default null"]))
)

(defn create-article-table []
  (sql/create-table :article
		    [:id           :int "IDENTITY" "PRIMARY KEY"]
		    [:title        :varchar "NOT NULL"] ; no default to empty
		    [:description  :varchar "NOT NULL"]
		    [:body         :varchar "NOT NULL"]
		    [:created      :datetime ] ; no default to 'now'
		    [:updated      :datetime ]
		    [:published    :datetime ]))

;; the actual value of db is decided in site.clj
(def db {:classname "org.hsqldb.jdbcDriver"
         :subprotocol "hsqldb"
         ;;:subname "c:/klang/wrk-clojure/projects/journal-server/articles.db"
         :subname "/home/klang/wrk-clojure/projects/journal-server/articles.db"})

(defn drop-article-table [] 
  (try 
   (sql/drop-table :article) 
   (catch Exception e)))

(comment
(sql/with-connection db (drop-article-table))
(sql/with-connection db (create-article-table))
)

(defn now [] (java.sql.Timestamp. (.getTime (java.util.Date.))))
(defmulti coerce (fn [dest-class src-inst] [dest-class (class src-inst)]))
(defmethod coerce [Integer String] [_ inst] (Integer/parseInt inst))
(defmethod coerce :default [dest-cls obj] (cast dest-cls obj))

;insert into article (title, description, body) values
;     ('Article 1', 'My first article',
;      '<p>Paragraph 1 in article 1</p><p>Another paragraph</p>'),
;     ('Article 2', 'My second article',
;      '<p>This article also has a paragraph</p><p>And another</p>');

(defn insert-samples []
  (let [timestamp (now)]
    (seq
     (sql/insert-values :article
			[:title :description :body :created :updated :published]
			["Article 1" "My first article" 
			 "<p>Paragraph 1 in article 1</p><p>Another paragraph</p>"
			 timestamp timestamp nil]
			["Article 2" "My second article" 
			 "<p>Paragraph 1 in article 2</p><p>Another paragraph</p>"
			 timestamp timestamp nil]))))

(defn sample-articles []
  (sql/with-connection db
    (drop-article-table)
    (create-article-table)
    (insert-samples)))

(defn select-articles []
  (sql/with-connection db
    (sql/with-query-results res ["select * from article"] (doall res))))

(defn sql-query [query]
  (sql/with-query-results res query (doall res)))

(defn articles [] 
  (sql/with-connection db
    (sql-query ["select * from article"])))

(defn last-created-id 
  "Extract the last created id. Must be called in a transaction
   that performed an insert. Expects HSQLDB return structure of
   the form [{:@p0 id}]."
  []
  (first (vals (first (sql-query ["CALL IDENTITY()"])))))

(defn insert-article [title description body]
  (let [timestamp (now)]
    (sql/with-connection db
      (sql/transaction
	(sql/insert-values 
	 :article
	 [:title :description :body :created :updated :published]
	 [title description body timestamp timestamp nil])
	(last-created-id)))))

;; functions to insert sample data

(defn insert-more-samples []
  (let [timestamp (now)]
    (insert-article 
     "Article 3" "My third article" 
      "<p>Paragraph 1 in article 3</p><p>Another paragraph</p>")
    (insert-article        
     "Article 4" "My fourth article" 
      "<p>Paragraph 1 in article 4</p><p>Another paragraph</p>")))

(defn sample-more-articles []
  (sql/with-connection db
    (drop-article-table)
    (create-article-table)
    (insert-samples)
    (insert-more-samples)))

;; populate the database with sample data

(sql/with-connection db (drop-article-table))
(sql/with-connection db (create-article-table))
(sample-more-articles)


