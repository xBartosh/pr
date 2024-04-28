package org.example;

import java.util.Random;


public class Obraz {

    private int sizeN;
    private int sizeM;
    private int liczbaZnakow;
    private char[][] tab;
    private char[] tabSymb;
    private int[] histogram;
    private int[] histogramParallel;
    private int[] histogramParallelBlock;

    public Obraz(int n, int m, int liczbaZnakow) {
        this.sizeN = n;
        this.sizeM = m;
        this.liczbaZnakow = liczbaZnakow;
        tab = new char[n][m];
        tabSymb = new char[liczbaZnakow];

        final Random random = new Random();

        // for general case where symbols could be not just integers
        for (int k = 0; k < liczbaZnakow; k++) {
            tabSymb[k] = (char) (k + 33); // substitute symbols
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                tab[i][j] = tabSymb[random.nextInt(liczbaZnakow)];  // ascii 33-127
                //tab[i][j] = (char)(random.nextInt(94)+33);  // ascii 33-127
                System.out.print(tab[i][j] + " ");
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");

        histogram = new int[liczbaZnakow];
        histogramParallel = new int[liczbaZnakow];
        histogramParallelBlock = new int[liczbaZnakow];
    }

    public char[] getTabSymb() {
        return tabSymb;
    }

    public void clear_histogram(int[] histogram) {
        for (int i = 0; i < liczbaZnakow; i++) {
            histogram[i] = 0;
        }
    }

    public void calculate_histogram() {
        for (int i = 0; i < sizeN; i++) {
            for (int j = 0; j < sizeM; j++) {

                // optymalna wersja obliczania histogramu, wykorzystujÄca fakt, Ĺźe symbole w tablicy
                // moĹźna przeksztaĹciÄ w indeksy tablicy histogramu
                // histogram[(int)tab[i][j]-33]++;

                // wersja bardziej ogĂłlna dla tablicy symboli nie utoĹźsamianych z indeksami
                // tylko dla tej wersji sensowne jest zrĂłwnoleglenie w dziedzinie zbioru znakĂłw ASCII
                for (int k = 0; k < liczbaZnakow; k++) {
                    if (tab[i][j] == tabSymb[k]) histogram[k]++;
                    //if(tab[i][j] == (char)(k+33)) histogram[k]++;
                }

            }
        }

    }

    public void calculateHistogramParallel(int pozycjaZnaku) {
        for (int i = 0; i < sizeN; i++) {
            for (int j = 0; j < sizeM; j++) {
                if (tab[i][j] == tabSymb[pozycjaZnaku]) histogramParallel[pozycjaZnaku]++;
            }

        }
    }

// uniwersalny wzorzec dla rĂłĹźnych wariantĂłw zrĂłwnoleglenia - moĹźna go modyfikowaÄ dla
// rĂłĹźnych wersji dekompozycji albo stosowaÄ tak jak jest zapisane poniĹźej zmieniajÄc tylko
// parametry wywoĹania w wÄtkach
//
//calculate_histogram_wzorzec(start_wiersz, end_wiersz, skok_wiersz,
//                            start_kol, end_kol, skok_kol,
//                            start_znak, end_znak, skok_znak){
//
//  for(int i=start_wiersz;i<end_wiersz;i+=skok_wiersz) {
//     for(int j=start_kol;j<end_kol;j+=skok_kol) {
//        for(int k=start_znak;k<end_znak;k+=skok_znak) {
//           if(tab[i][j] == tab_symb[k]) histogram[k]++;
//


    public void printHistogram() {
        for (int i = 0; i < liczbaZnakow; i++) {
            System.out.print(tabSymb[i] + " " + histogram[i] + "\n");
            //System.out.print((char)(i+33)+" "+histogram[i]+"\n");
        }
    }

    public void printHistogramParallel() {
        for (int i = 0; i < liczbaZnakow; i++) {
            System.out.print(tabSymb[i] + " " + histogramParallel[i] + "\n");
        }
    }

    public void printHistogramParallelBlock() {
        for (int i = 0; i < liczbaZnakow; i++) {
            System.out.print(tabSymb[i] + " " + histogramParallelBlock[i] + "\n");
        }
    }

    public synchronized void printHistogramForSymbol(int pozycjaZnaku) {
        System.out.printf("Wątek %s: %c ", Thread.currentThread().getId(), tabSymb[pozycjaZnaku]);
        for (int i = 0; i < histogramParallel[pozycjaZnaku]; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    public boolean compareHistogramsParallel() {
        System.out.println("\nHistogram sekwencyjny vs histogram równoległy");
        boolean histogramsMatch = true;
        for (int i = 0; i < liczbaZnakow; i++) {
            System.out.printf("Sekwencyjny: %c %d, równoległy: %c %d\n", tabSymb[i], histogram[i], tabSymb[i], histogramParallel[i]);
            if (histogram[i] != histogramParallel[i]) {
                histogramsMatch = false;
            }
        }
        return histogramsMatch;
    }

    public boolean compareHistogramsParallelBlock() {
        System.out.println("\nHistogram sekwencyjny vs histogram równoległy blokowy");
        boolean histogramsMatch = true;
        for (int i = 0; i < liczbaZnakow; i++) {
            System.out.printf("Sekwencyjny: %c %d, równoległy blokowy: %c %d\n", tabSymb[i], histogram[i], tabSymb[i], histogramParallelBlock[i]);
            if (histogram[i] != histogramParallelBlock[i]) {
                histogramsMatch = false;
            }
        }
        return histogramsMatch;
    }

    public void calculateHistogramParallelBlock(int start, int end) {
        for (int i = 0; i < sizeN; i++) {
            for (int j = 0; j < sizeM; j++) {
                for (int k = start; k < end; k++) {
                    if (tab[i][j] == tabSymb[k]) histogramParallelBlock[k]++;
                }
            }
        }
    }

    public synchronized void printHistogramForBlock(int start, int end) {
        for (int i = start; i < end; i++) {
            System.out.printf("Wątek %s: %c ", Thread.currentThread().getId(), tabSymb[i]);
            for (int j = 0; j < histogramParallelBlock[i]; j++) {
                System.out.print("=");
            }
            System.out.println();
        }
    }
}