(ns hello-world
  (:use compojure))

(defroutes greeter
  (GET "/"
    (html [:h1 "Hello World"])))

(run-server {:port 8080}
  "/*" (servlet greeter))
