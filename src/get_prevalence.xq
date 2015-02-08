declare variable $n as xs:string external; (: command line query, to be entered as "n" variable :)
declare option output:item-separator "&#xa;";  (: Each element would be in new line :)

let $cmd := "//inheritance"

let $apiPath := 
for $db in db:list() (: Run input query, on every XML document in every Database:)
let $query :=
  "declare variable $db external; " || (: Assign dynamic variables to generate query, to be used in eval :)
  "db:open($db)" || $cmd
return xquery:eval($query,
  map { 'db': $db, 'query': $cmd })

let $clients :=
for $elem in $apiPath
return db:path($elem)

(: return no of files matched :)
return distinct-values(count($clients))
