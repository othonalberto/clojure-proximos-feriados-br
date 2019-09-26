(defn formataData
  "Recebe uma data no formato ANO-MES-DIA e retorna padrão BR (DIA/MES/ANO)"
  [dataSemFormatacao]
  (def separadas (clojure.string/split dataSemFormatacao #"-"))
  (str (get separadas 2) "/" (get separadas 1) "/" (get separadas 0)))

(defn consomeAPI
  [ano & [quantidadeFeriados]]
  (if (and quantidadeFeriados (not (neg? quantidadeFeriados)))
    (def n quantidadeFeriados)
    (def n 1))

  (def fakeData "1997-09-25")

  (formataData fakeData))

(println (str ">>>> " (consomeAPI 2019 -1)))