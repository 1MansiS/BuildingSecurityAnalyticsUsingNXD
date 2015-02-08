declare variable $n as xs:string external; (: command line query, to be entered as "n" variable :)
declare option output:item-separator "&#xa;";  (: Each element would be in new line :)

let $apiPath := 
for $db in db:list()  (: Run input query, on every XML document in every Database:)
let $query :=
  "declare variable $db external; " || (: Assign dynamic variables to generate query, to be used in eval :)
  "db:open($db)" || $n
return xquery:eval($query,
  map { 'db': $db, 'query': $n })

(: Unique path name:)
let $clients :=
for $elem in $apiPath
return db:path($elem)

return distinct-values($clients)
