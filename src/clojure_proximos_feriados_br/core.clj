(ns clojure-proximos-feriados-br.core
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json])
  (:require [clojure.set :as set]))

(defn consomeAPI
  [ano n]

  (client/get (str "https://date.nager.at/api/v2/publicholidays/" ano "/BR")))

(defn feriadoValido?
  [dataFeriadoEntrada]

  (def dataFeriado (.parse
                    (java.text.SimpleDateFormat. "yyyy-MM-dd")
                    dataFeriadoEntrada))

  (def dataAtual (new java.util.Date))

  (def resultado (compare dataFeriado dataAtual))
  (if (pos-int? resultado)
    true
    false))

(defn formataSaida
  [feriado]
  {:nome (get feriado :localName),
   :data (get feriado :date)})

(defn obtemFeriados
  [dataHoje & [quantidadeFeriados]]

  (if (pos-int? quantidadeFeriados)
    (def n quantidadeFeriados)
    (def n 1))

  (def dataSeparada (clojure.string/split dataHoje #"/"))
  (def ano (Integer/parseInt (get dataSeparada 2)))

  (def retornoAPI (consomeAPI ano n))

  (def feriados
    (json/read-str
     (get retornoAPI :body) :key-fn keyword))

  (def feriadosValidos (filter (every-pred (comp feriadoValido? :date))
                               feriados))

  (map formataSaida (take n feriadosValidos)))

(defn -main
  [n]
  (println (obtemFeriados
            (.format (java.text.SimpleDateFormat. "MM/dd/yyyy") (new java.util.Date))
            (Integer/parseInt n))))
