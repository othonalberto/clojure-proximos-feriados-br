(ns clojure-proximos-feriados-br.core
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json]))

(defn formataData
  "Recebe uma data no formato ANO-MES-DIA e retorna padrão BR (DIA/MES/ANO)"
  [dataSemFormatacao]
  (def separadas (clojure.string/split dataSemFormatacao #"-"))
  (str (get separadas 2) "/" (get separadas 1) "/" (get separadas 0)))

(defn consomeAPI
  [ano n]

  (def resposta (client/get (str "https://date.nager.at/api/v2/publicholidays/" ano "/BR"))) ; PRECISA SER ASSINCRONO
  (if (> n (count resposta))
    (def proximoAno (client/get (str "https://date.nager.at/api/v2/publicholidays/" (+ ano 1) "/BR")))
    (def proximoAno (empty {1 2})))

  (vector resposta proximoAno))

(defn feriadoValido?
  [feriado mes dia]

  (println feriado)
  (println "\n------------------\n"))

(defn obtemFeriados
  [dataHoje & [quantidadeFeriados]]

  (if (and quantidadeFeriados (pos-int? quantidadeFeriados))
    (def n quantidadeFeriados)
    (def n 1))

  (def dataSeparada (clojure.string/split dataHoje #"/"))
  (def ano (get dataSeparada 0))
  (def mes (read-string (get dataSeparada 1)))
  (def dia (read-string (get dataSeparada 2)))

  (def retornoAPI (get (consomeAPI ano n) 0))
  ; (println retornoAPI)

  (def feriados
    (json/read-str (get retornoAPI :body) :key-fn keyword))

  ; (def proximosFeriadosAno (filter (feriadoValido? feriados mes ano)))
  ; (filter (feriadoValido? (get feriados) mes ano))
  ; (filter #(= "yellow" (second %)) flowers)
  ; (filter (every-pred (feriadoValido? feriados mes ano))
  (def proximosFeriadosAno ((filter (every-pred (comp true :global))) feriados))
  (println proximosFeriadosAno)

  ; (if (empty? (get retornoAPI 1))
  ;   (def todos (apply merge '((get retornoAPI 0) (get retornoAPI 1) mes ano)))
  ;   (def todos (get retornoAPI 0)))

  ; (retornoAPI))
  )

(defn -main
  [n]
  ; (println (obtemFeriados "2019/9/15" n)))
  (obtemFeriados "2019/9/15" n))
