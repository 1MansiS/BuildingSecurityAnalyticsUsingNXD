import org.basex.core.*;
import org.basex.core.cmd.*;

import java.io.* ;

class createDB {

	static Context context = new Context();
	public static void main(String args[]) throws BaseXException {

		// Dir where all meta-data files are stored
		String pathToXmlFiles = args[0] ;
		File[] files = new File(pathToXmlFiles).listFiles();	

		List list = new List();

		int maxDB = 0 ;
		int noOfDoc = 0 ;
		String dbNameToAdd = "" ;

		// For first import to database, picking database name
		if (list.list(context).size() == 0) {
			new CreateDB("bi_output_1").execute(context) ;
			new Close().execute(context);
			new Open("bi_output_1").execute(context) ;
		}

		String query = "" ;

		for(File file : files) {
			if (file.getName().endsWith("xml")) {		
				maxDB = list.list(context).size();
				new Open("bi_output_" + maxDB).execute(context);
				// Call BaseX command to open latest database created and see how many documents it already hase
				query = "db:info('bi_output_" + maxDB +"')//documents/text()" ;
				noOfDoc = Integer.parseInt(query(query));

				// Store max 50 documents per db
				if (noOfDoc == 50) {
					dbNameToAdd = "bi_output_" + (maxDB + 1);
					new CreateDB(dbNameToAdd).execute(context); // create new db
					new Close().execute(context);
					new Open(dbNameToAdd).execute(context); 
				
				}
				else {
					dbNameToAdd = "bi_output_" + maxDB ; // Latest db created doesnt yet have 50 documents, so add here
				}
			
				query = "db:add('" + dbNameToAdd + "','" + file+"')"  ; // Based on above, add in new db or current db
				try {
					query(query) ;
					System.out.println("Adding " + file + " in DB " + dbNameToAdd ) ;
				} catch(BaseXException be) {System.out.println("Problem addiing " + file ); }
			}
		}
		new CreateIndex("attribute").execute(context); // Most of my queries are attribute based, so creating index on that.
	}	

	static String query(final String query) throws BaseXException {
	    return new XQuery(query).execute(context);
	  }

}
