
(defn create-article []
  (create-table :article
		[:id           :serial primary key]
		[:title        :text "NOT NULL" unique check (trim(title)  '')]
		[:description  :text "NOT NULL" default '']
		[:body         :text "NOT NULL" default '']
		[:created      :timestamp "NOT NULL" default now()]
		[:updated      :timestamp "NOT NULL" default now()]
		[:published    :timestamp "NULL" default null]))


