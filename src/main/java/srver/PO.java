package srver;

/**
 * Created by Ксения on 12.12.2016.
 */
public class PO {

        private String name;
        private String version;
        private String PK;
        public PO(String PK, String name, String version){
            this.name=name;
            this.version=version;
            this.PK=PK;
        }
        public String getName(){
            return this.name;
        }
        public String getVersion(){
            return this.version;
        }
    }


