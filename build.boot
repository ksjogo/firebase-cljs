(defn read-file   [file] (read-string (slurp file)))
(defn get-deps    []     (read-file "./dependencies.edn"))
(defn get-devdeps []     (read-file "./dev_dependencies.edn"))

(set-env!
 :dependencies   (get-deps)
 :resource-paths #{"src"})

(require
 '[adzerk.bootlaces :refer :all]
 '[hoplon.boot-hoplon :refer :all]
 '[boot-semver.core :refer :all]
 '[funcool.boot-codeina :refer :all]
 '[tolitius.boot-check :as check])

(task-options!
 pom    {:project 'degree9/firebase-cljs
         :description "Firebase bindings for CLJS"
         :url         ""
         :version (get-version)
         :scm {:url ""}}
 apidoc {:version (get-version)
         :reader :clojurescript
         :title "firebase-cljs"
         :sources #{"src"}
         :description "Cljs bindings for Firebase API v3."})

(deftask tests
  "Run code tests."
  []
  (comp
    (check/with-kibit)
    (check/with-yagni)))

(deftask deploy
  "Build project for deployment to clojars."
  []
  (comp
    ;(version :minor 'inc :patch 'zero)
    (build-jar)
    (push-release)))

(deftask dev
  "Build project for development."
  []
  (comp
    (watch)
    ;(checkout :dependencies (get-devdeps))
    (version :no-update true
             :minor 'inc
             :patch 'zero
             :pre-release 'snapshot)
    (build-jar)
    (apidoc)
    (tests)))