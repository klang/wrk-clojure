(ns site)

; Server settings
(jetty/defserver journal-server
  {:port 8080}
  "/*" (servlet/servlet journal-servlet))

; Command to start the server
(jetty/start journal-server)
;(jetty/stop journal-server)