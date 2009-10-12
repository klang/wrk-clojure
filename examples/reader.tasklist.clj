(ns reader.tasklist
  (:gen-class
   :extends org.xml.sax.helpers.DefaultHandler
   :state state
   :init init)
  (:use [clojure.contrib.duck-streams :only (reader)])
  (:import [java.io File]
           [org.xml.sax InputSource]
	   [org.xml.sax.helpers DefaultHandler]
	   [javax.xml.parsers SAXParserFactory]))

;(compile 'reader.tasklist)
(declare task-list)

(defn task-list [arg]
  (let [handler (new reader.tasklist)]
    (.. SAXParserFactory newInstance newSAXParser
	(parse (InputSource. (reader (File. arg)))
	       handler))
    @(.state handler)))

