declare variable $n as xs:string external;
declare option output:item-separator "&#xa;";

let $apiPath := 
for $db in db:list()
let $query :=
  "declare variable $db external; " ||
  "db:open($db)" || $n
return xquery:eval($query,
  map { 'db': $db, 'query': $n })

let $clients :=
for $elem in $apiPath
return db:path($elem)

return distinct-values($clients)
