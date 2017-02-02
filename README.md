## Introduzione

Il progetto visualizza una mappa google sulla propria applicazione android, e tramite una slide menu viene visualizzato un elenco di punti di interesse, dopo aver selezionato il punto di interesse, l'applicazione ritorna tutti i punti di interesse che trova nelle vicinanze della sua posizione

## Struttura Progetto

il progetto è strutturato in modo tale da essere il più flessibile possibile. la suddivisione è la seguente:

  * activities: 
     * nella drawer activity c’è tutto il codice del menu a comparsa, con i bottoni per cercare i punti di interesse
     * homeActivity(mappa della applicazione). La home activity disegna sulla mappa(ha un service separato per richiamare il servizo in qualsiasi activity)
  * adapters
    * nel menu i bottoni sono tutti allo stesso modo, viene impostato quindi un layout con quella grafica, l’adapter prende un array e all’interno di una recicle view.  quindi nel recycleView c’è una lista di oggetti che hanno dei layout prestabiliti, e scorrendo una lista riempie i vari oggetti.
  
  * receivers
   *  In android per comunicare far comunicare delle activity tra loro, oppure tra service e activity, si utilizzano i broadcast messagge. chi vuole inviare qualcosa invia un messaggio tramite intent message, chi vuole ricevere quella tipologia di messaggio, deve implementare un broadcast receiver, e implementare un intent filter. Nella cartella receiver viene implementato questo ragionamento di base.
  
  * services
    * viene interrogato google place, si prende il messaggio di ritorno e lo passa alla home activity(per i punti di interesse).
    
  * utils
   * nella cartella utils ci sono tutti le classi che servono da utilità per far girare l'applicazione, in questo caso:
      1. dataParser(esegue il parser dell'oggetto json ricevuto da google)
      2. download URL(serve per collegarsi a google stabilendo una connessione http)
      3. getNearbyPlacesData(restituisce l'elenco dei punti di interesse vicini)

## Tecnologie
 * java
 * android
 * android studio
 * google place api

## Installazione
1. scaricare l'applicazione da github
2. aver installato precedentemente android studio
3. integrare il progetto nella propria workspace
4. installare gli sdk necessari(consigliati dalla 4.4 fino all'ultima release)
5. generare una propria api key dal seguente sito https://developers.google.com/places/web-service/get-api-key e inserirla nel file AndroidManifest.xml(altrimenti la mappa non viene caricata)
