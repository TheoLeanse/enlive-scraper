(ns track-scraper.core
  (:require [net.cgrand.enlive-html :as enlive]
            [clojure.string :as str]))

(def ^:dynamic *base-url* "https://djfriendly.co.uk/stocklist.php?show=")
(def ^:dynamic *selectors* [:tr :> :td :> :span])
(def ^:dynamic *artist-selectors* [:tr :> :td :> :span.text13b])
(def ^:dynamic *album-selectors* [:tr :> :td :> :span.text13])

(def ^:dynamic *general-selectors* [:td :> :table])

(def genres ["arthurrussell", "Gospel", "electronic", "jazz", "soulfunk", "singles", "dsingles", "disco", "discouk", "tbreak", "disconotdisco", "Italo", "danceclassic", "afro", "afro12", "latin", "brazil", "soundtracks", "breaks", "funky", "oldrap", "electro", "rap", "raplp", "chicago", "detroit", "newyork", "ukhouse", "Mod/Latin", "crossover", "Group/Bal", "Jazz7s", "Tropical", "reggaenot", "NSSingles", "MTown", "5060soul", "gogo", "spokenword", "reggae", "library", "rock", "TRO"])

(defn compose-url [genre]
  (str *base-url* genre))

(def urls (map compose-url genres))

(defn fetch-url [url]
  (enlive/html-resource (java.net.URL. url)))

(defn records [url selector]
  (map enlive/text (enlive/select (fetch-url url) selector)))

(defn zip-artist-album [url]
  (map vector (records url *artist-selectors*)
       (records url *album-selectors*)))

(map zip-artist-album urls)