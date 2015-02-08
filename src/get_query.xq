declare variable $n as xs:string external; (: command line query, to be entered as "n" variable :)
declare option output:item-separator "&#xa;"; (: Each element would be in new line :)

(: Run input query, on every XML document in every Database:)
for $db in db:list()
	(: Assign dynamic variables to generate query, to be used in eval :)
	let $query := "declare variable $db external; " || "db:open($db)" || $n
	return xquery:eval($query,map { 'db': $db, 'query': $n })
