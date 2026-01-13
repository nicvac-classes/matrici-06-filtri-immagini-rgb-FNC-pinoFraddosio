//LEGGERE LE ISTRUZIONI NEL FILE ESERCIZIO.md

//Import di Classi Java necessarie al funzionamento del programma
import utils.ImageRGB;

// Classe principale, con metodo main
class Esercizio {

    //Elimina le componenti Verde e Blu, lasciando solo il Rosso
    public static void filtroRosso( int[][] R, int[][] G, int[][] B ) {

        int row = R.length;
        int col = R[0].length;

        for(int i = 0; i < row; ++i){
            for(int j = 0; j < col; ++j){
                G[i][j] = 0;
                B[i][j] = 0;
            }
        }

    }
    
    //Bianco e nero: R, G, B impostati alla media dei valori
    public static void filtroBW( int[][] R, int[][] G, int[][] B ) {
        
        int row = R.length;
        int col = R[0].length;

        for(int i = 0; i < row; ++i){
            for(int j = 0; j < col; ++j){
                int media = (R[i][j] + G[i][j] + B[i][j]) / 3;

                R[i][j] = media;
                G[i][j] = media;
                B[i][j] = media;
            }
        }
    }

    //I pixel vicino al centro hanno più luminosità
    public static void filtroCentro(int[][] R, int[][] G, int[][] B) {
        int centroRow = (R.length) / 2;
        int centroCol = (R[0].length) / 2;

        double maxDistanza = Math.sqrt(centroRow * centroRow + centroCol + centroCol);

        int row = R.length;
        int col = R[0].length;

        for(int i = 0; i < row; ++i){
            for(int j = 0; j < col; ++j){
                double distanzaRow = Math.abs(i - centroRow);
                double distanzaCol = Math.abs(j - centroCol);

                double distanza = Math.sqrt(distanzaRow * distanzaRow + distanzaCol * distanzaCol);

                double cambioIntensita = 1.0 - (distanza/maxDistanza);

                R[i][j] *= cambioIntensita;
                G[i][j] *= cambioIntensita;
                B[i][j] *= cambioIntensita;
            }
        }
    }

    //Effetto glitch (rosso e blu sfasati)
    public static void filtroGlitch(int[][] R, int[][] G, int[][] B) {
        int row = R.length;
        int col = R[0].length;

        int[][] Bnew = new int[row][col];
        int[][] Rnew = new int[row][col];

        for(int i = 0; i < row; ++i){
            for(int j = 0; j < col; ++j){
                Bnew[i][j] = B[i][j];
                Rnew[i][j] = R[i][j];

                B[i][j] = 0;
                R[i][j] = 0;
            }
        }

        int offset = 20;

        for(int i = 0; i < row; ++i){
            for(int j = 0; j < col; ++j){
                int offsetRowRosso = i - offset;
                int offsetColRosso = j - offset;

                int offsetRowBlu = i + offset;
                int offsetColBlu = j + offset;

                if(offsetRowBlu < row && offsetColBlu < col){
                    B[offsetRowBlu][offsetColBlu] = Bnew[i][j];
                }

                if(offsetRowRosso >= 0 && offsetColRosso >= 0){
                    R[offsetRowRosso][offsetColRosso] = Rnew[i][j];
                }
            }
        }
    }

    //Oscura i pixel che non sono Mare (prevalenza blu)
    public static void filtroMare(int[][] R, int[][] G, int[][] B) {
      
        int row = R.length;
        int col = R[0].length;

        for(int i = 0; i < row; ++i){
            for(int j = 0; j < col; ++j){
                if(!(R[i][j] < 127 && G[i][j] < 127 && B[i][j] >= 127)){
                    R[i][j] = 0;
                    G[i][j] = 0;
                    B[i][j] = 0;
                }
            }
        }
    }

    //Oscura i pixel che non sono Spiaggia (prevalenza giallo)
    public static void filtroSpiaggia(int[][] R, int[][] G, int[][] B) {
     
        int row = R.length;
        int col = R[0].length;

        for(int i = 0; i < row; ++i){
            for(int j = 0; j < col; ++j){
                if(!(R[i][j] >= 127 && G[i][j] >= 127 && B[i][j] < 127)){
                    R[i][j] = 0;
                    G[i][j] = 0;
                    B[i][j] = 0;
                }
            }
        }
    }

    //Oscura i pixel che non sono Vegetazione (prevalenza verde)
    public static void filtroVegetazione(int[][] R, int[][] G, int[][] B) {

        int row = R.length;
        int col = R[0].length;

        for(int i = 0; i < row; ++i){
            for(int j = 0; j < col; ++j){
                if(!(R[i][j] < 100 && G[i][j] >= 100 && B[i][j] < 100)){
                    R[i][j] = 0;
                    G[i][j] = 0;
                    B[i][j] = 0;
                }
            }
        }
    }

    
    public static void main(String args[]) {

        try {
            //Attiva qui uno dei due file immagine da leggere:
            String nomeFileIn = "faro";
            //String nomeFileIn = "foto_aerea";

            //Conversione dell'immagine in matrici parallele R, G, B
            ImageRGB.RGB rgb = ImageRGB.extractRGB("immagini/" + nomeFileIn + ".png");
            int[][] R = rgb.R();
            int[][] G = rgb.G();
            int[][] B = rgb.B();

            //Qui R, G, B contengono i livelli di rosso, verde, blu per ogni pixel.
            //I valori vanno da 0 a 255, da bassa intensità ad alta intensità.
            //Visualizzo la dimensione dell'immagine considerando il rosso (verde e blu hanno la stessa dimensione).
            System.out.println("Dimensione immagine: " + R.length+ "x" + R[0].length );

            //Chiama qui la funzione filtro, passando come parametri le matrici R,G,B da modificare, ad es.:
            //filtroRosso(R, G, B);
            //___
            filtroRosso(R, G, B);
            //Assegna il nome del file di output (immagine con filtro), ad es. "faro_rosso.png":
            String nomeFileOut = "filtro_rosso.png";

            ImageRGB.writeRGB(R, G, B, nomeFileOut);
 
        } catch( Exception e) {
            e.printStackTrace();
        }

        try{
            String nomeFileIn = "faro";

            ImageRGB.RGB rgb = ImageRGB.extractRGB("immagini/" + nomeFileIn + ".png");
            int[][] R = rgb.R();
            int[][] G = rgb.G();
            int[][] B = rgb.B();

            String nomeFileOut = "filtro_BW.png";

            filtroBW(R, G, B);

            ImageRGB.writeRGB(R, G, B, nomeFileOut);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{

            String nomeFileIn = "faro";

            ImageRGB.RGB rgb = ImageRGB.extractRGB("immagini/" + nomeFileIn + ".png");
            int[][] R = rgb.R();
            int[][] G = rgb.G();
            int[][] B = rgb.B();

            String nomeFileOut = "filtro_centro.png";

            filtroCentro(R, G, B);

            ImageRGB.writeRGB(R, G, B, nomeFileOut);
        }catch(Exception e){
            e.printStackTrace();
        }
        

        try{

            String nomeFileIn = "faro";

            ImageRGB.RGB rgb = ImageRGB.extractRGB("immagini/" + nomeFileIn + ".png");
            int[][] R = rgb.R();
            int[][] G = rgb.G();
            int[][] B = rgb.B();

            String nomeFileOut = "filtro_centro.png";

            filtroCentro(R, G, B);

            ImageRGB.writeRGB(R, G, B, nomeFileOut);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{

            String nomeFileIn = "faro";

            ImageRGB.RGB rgb = ImageRGB.extractRGB("immagini/" + nomeFileIn + ".png");
            int[][] R = rgb.R();
            int[][] G = rgb.G();
            int[][] B = rgb.B();

            String nomeFileOut = "filtro_centro_e_BW.png";

            filtroBW(R, G, B);
            filtroCentro(R, G, B);

            ImageRGB.writeRGB(R, G, B, nomeFileOut);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{

            String nomeFileIn = "faro";

            ImageRGB.RGB rgb = ImageRGB.extractRGB("immagini/" + nomeFileIn + ".png");
            int[][] R = rgb.R();
            int[][] G = rgb.G();
            int[][] B = rgb.B();

            String nomeFileOut = "filtro_glitch.png";

            filtroGlitch(R, G, B);

            ImageRGB.writeRGB(R, G, B, nomeFileOut);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{

            String nomeFileIn = "foto_aerea";

            ImageRGB.RGB rgb = ImageRGB.extractRGB("immagini/" + nomeFileIn + ".png");
            int[][] R = rgb.R();
            int[][] G = rgb.G();
            int[][] B = rgb.B();

            String nomeFileOut = "filtro_mare.png";

            filtroMare(R, G, B);

            ImageRGB.writeRGB(R, G, B, nomeFileOut);
        }catch(Exception e){
            e.printStackTrace();
        }


        try{

            String nomeFileIn = "foto_aerea";

            ImageRGB.RGB rgb = ImageRGB.extractRGB("immagini/" + nomeFileIn + ".png");
            int[][] R = rgb.R();
            int[][] G = rgb.G();
            int[][] B = rgb.B();

            String nomeFileOut = "filtro_spiaggia.png";

            filtroSpiaggia(R, G, B);

            ImageRGB.writeRGB(R, G, B, nomeFileOut);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{

            String nomeFileIn = "foto_aerea";

            ImageRGB.RGB rgb = ImageRGB.extractRGB("immagini/" + nomeFileIn + ".png");
            int[][] R = rgb.R();
            int[][] G = rgb.G();
            int[][] B = rgb.B();

            String nomeFileOut = "filtro_vegetazione.png";

            filtroVegetazione(R, G, B);

            ImageRGB.writeRGB(R, G, B, nomeFileOut);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

//LEGGERE LE ISTRUZIONI NEL FILE ESERCIZIO.md