import org.basex.core.*;
import org.basex.core.cmd.*;

import java.io.* ;

class createDB {

	static Context context = new Context();
	public static void main(String args[]) throws BaseXException {

		String pathToXmlFiles = args[0] ;
		File[] files = new File(pathToXmlFiles).listFiles();	

		List list = new List();

		int maxDB = 0 ;
		int noOfDoc = 0 ;
		String dbNameToAdd = "" ;

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
				query = "db:info('bi_output_" + maxDB +"')//documents/text()" ;
				noOfDoc = Integer.parseInt(query(query));

				if (noOfDoc == 50) {
					dbNameToAdd = "bi_output_" + (maxDB + 1);
					new CreateDB(dbNameToAdd).execute(context);
					new Close().execute(context);
					new Open(dbNameToAdd).execute(context);
				
				}
				else {
					dbNameToAdd = "bi_output_" + maxDB ;
				}
			
				query = "db:add('" + dbNameToAdd + "','" + file+"')"  ;
				try {
					query(query) ;
					System.out.println("Adding " + file + " in DB " + dbNameToAdd ) ;
				} catch(BaseXException be) {System.out.println("Problem addiing " + file ); }
			}
		}
		new CreateIndex("attribute").execute(context);
	}	

	static String query(final String query) throws BaseXException {
	    return new XQuery(query).execute(context);
	  }

}
