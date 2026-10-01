Zagon aplikacije
1. uporabite ukaz javac -d out src/*.java
2. nato podajte vnos za izvajanje programa v obliki z argumenti java -cp out Main 1 2 relative
3. ali pa izvedite zagon programa z java -cp out Main in ročno vnesite podatke
4. za izvajanje testov odprite Test/ScheduleTest.java in kliknite na zeleno puščico (Run) ob imenu razreda.


Kako bi naredil nalogo zahtevnejšo?
1. Predvsem bi namesto text datotek uporabil shranjevanje podatkov v podatkovno bazo. Z tem bi seveda izboljšal
hitrost pridobivanja podatkov.
2. Izdelal bi uporabniški vmesnik, ki bi lepše prikazal same prihode in odhode avtobusov. UI je zelo pomemben
iz vidika uporabikov saj je to ob funkcionalnosti spletne aplikacije zelo pomembno.
3. Omogočil bi tudi shranjevanje podatkov tudi v odklopljenem načinu dela spletne aplikacije v primeru, da uporabik
npr. izgubi spletno povezavo se podatki predpomnijo in še vedno lahko dostopa do voznega reda.