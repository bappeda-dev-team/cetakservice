package cc.kertaskerja.cetakservice.pdf.pokin;

/** Satu potongan vertikal dari layout pohon yang dicetak pada satu halaman. */
public record PageSegment(float startY, int number, int total) {
}
