(ns track-scraper.core
  (:require [net.cgrand.enlive-html :as enlive]
            [clojure.string :as str]))

(def ^:dynamic *base-url* "https://djfriendly.co.uk/stocklist.php?show=")

(def ^:dynamic *artist-selectors* [:tr :> :td :> :span.text13b])
(def ^:dynamic *album-selectors* [:tr :> :td :> :span.text13])

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

(def artist-album-vectors (map zip-artist-album urls))

(defn discogs-search
  [artist-album]
  (str "https://api.discogs.com/database/search?title=" (apply str artist-album) "&token=" (System/getenv "DISCOGS_TOKEN")))

(defn genre-strings [list-of-vectors]
  (map discogs-search list-of-vectors))

(def search-urls-by-section (map genre-strings artist-album-vectors))

(defn search-discogs [url]
  ;; TODO
  (url))

(map #(map search-discogs %) search-urls-by-section)
