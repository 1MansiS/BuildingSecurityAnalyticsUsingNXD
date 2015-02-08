declare variable $n as xs:string external;
declare option output:item-separator "&#xa;";

let $cmd := "//inheritance"

let $apiPath := 
for $db in db:list()
let $query :=
  "declare variable $db external; " ||
  "db:open($db)" || $cmd
return xquery:eval($query,
  map { 'db': $db, 'query': $cmd })

let $clients :=
for $elem in $apiPath
return db:path($elem)

return distinct-values(count($clients))
