;; this file is run by leiningen
(println "use comment in user.clj")
(comment
  (use 'clojure.contrib.repl-utils)
  (load-file "./src/article.clj")
  (load-file "./src/site.clj")
  (load-file "./src/start-server.clj")
  (load-file "./src/eriklavigne.clj")
)

