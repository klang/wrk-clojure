(ns article
  (:use clojure.contrib.sql))

(comment 
(defn create-article []
  (create-table :article
		[:id           :serial "primary key"]
		[:title        :text "NOT NULL" "unique check (trim(title) '')"]
		[:description  :text "NOT NULL" "default ''"]
		[:body         :text "NOT NULL" "default ''"]
		[:created      :timestamp "NOT NULL" "default now()"]
		[:updated      :timestamp "NOT NULL" "default now()"]
		[:published    :timestamp "NULL" "default null"]))
)

(defn create-article-table []
  (create-table :article
		[:id           :int "IDENTITY" "PRIMARY KEY"]
		[:title        :varchar "NOT NULL"] ; no default to empty
		[:description  :varchar "NOT NULL"]
		[:body         :varchar "NOT NULL"]
		[:created      :datetime ] ; no default to 'now'
		[:updated      :datetime ]
		[:published    :datetime ]))

(def db {:classname "org.hsqldb.jdbcDriver"
         :subprotocol "hsqldb"
         :subname "c:/klang/wrk-clojure/projects/journal-server/articles.db"})

(defn drop-article-table [] 
  (try 
   (drop-table :article) 
   (catch Exception e)))

(with-connection db (create-article-table))
(with-connection db (drop-article-table))

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
     (insert-values :article
		    [:title :description :body :created :updated :published]
		    ["Article 1" "My first article" 
		     "<p>Paragraph 1 in article 1</p><p>Another paragraph</p>"
		     timestamp timestamp nil]
		    ["Article 2" "My second article" 
		     "<p>Paragraph 1 in article 2</p><p>Another paragraph</p>"
		     timestamp timestamp nil]
		    ))))

(defn sample-articles []
  (with-connection db
    (drop-article-table)
    (create-article-table)
    (insert-samples)))

(defn select-articles []
  (with-connection db
    (with-query-results res ["select * from article"] (doall res))))
