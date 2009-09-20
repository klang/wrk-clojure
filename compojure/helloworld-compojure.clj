(ns example-app
	(:use compojure.http.servlet)
	(:use compojure.server.jetty))

(defn hello-world [request]
	{:status 200
	 :headers {}
	 :body "Hello World"})

(run-server {:port 8080}
	    "/*" (servlet hello-world))