Zagon aplikacije
1. odpremo cmd in v izbrano mapo
2. kloniramo projekt git clone https://github.com/tabakovicamar12/Schedule_plan.git 
3. odpremo IntelliJ IDEA 
4. Kliknemo na open v orodni vrstici
5. Odpremo datoteko kjer je kloniran projekt
6. Odpremo terminal v IntelliJ IDEA
7. Vnos komand javac -d out src/*.java 
8. java -cp out Main 2 2 relative

Zagon testov
1. V mapi Test izberemo ScheduleTest
2. V kodi nato namestimo knjiznice postavimo se na rdece obarvane na katere se postavimo @Test, jupiter, DisplayName tj. zahtevajo knjiznice JUnit4 ali 5 in namestimo potrebne
3. V primeru, da se dodajo obe knjiznice Unit izbrisemo med import na vrhu kode vrstico import org.junit.jupiter.api.Test;
4. Zagon kode na Run 


Kako bi naredil nalogo zahtevnejšo?
1. Predvsem bi namesto text datotek uporabil shranjevanje podatkov v podatkovno bazo. Z tem bi seveda izboljšal
hitrost pridobivanja podatkov.
2. Izdelal bi uporabniški vmesnik, ki bi lepše prikazal same prihode in odhode avtobusov. UI je zelo pomemben
iz vidika uporabikov saj je to ob funkcionalnosti spletne aplikacije zelo pomembno.
3. Omogočil bi tudi shranjevanje podatkov tudi v odklopljenem načinu dela spletne aplikacije v primeru, da uporabik
npr. izgubi spletno povezavo se podatki predpomnijo in še vedno lahko dostopa do voznega reda.
