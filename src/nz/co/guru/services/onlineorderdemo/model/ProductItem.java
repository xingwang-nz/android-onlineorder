package nz.co.guru.services.onlineorderdemo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ProductItem implements Serializable {

    private long code;

    private BigDecimal unitPrice;

    private ProductUnit unit;

    private int quantity;

    private String packaging;

    private Language language = Language.ENG;

    private Map<Language, String> nameMap;

    private Map<Language, String> noteMap;

    public ProductItem() {
    }

    public ProductItem(final long code, final Map<Language, String> nameMap, final ProductUnit unit, final BigDecimal unitPrice, final String packaging) {
        this(code, nameMap, unit, unitPrice, packaging, new HashMap<Language, String>());
    }

    public ProductItem(final long code, final Map<Language, String> nameMap, final ProductUnit unit, final BigDecimal unitPrice, final String packaging,
            final Map<Language, String> noteMap) {
        this.code = code;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.packaging = packaging;
        this.noteMap = noteMap;
        this.nameMap = nameMap;

    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(final BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ProductUnit getUnit() {
        return unit;
    }

    public void setUnit(final ProductUnit unit) {
        this.unit = unit;
    }

    public long getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (code ^ code >>> 32);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ProductItem)) {
            return false;
        }
        final ProductItem other = (ProductItem) obj;
        if (code != other.code) {
            return false;
        }
        return true;
    }

    public void setCode(final long code) {
        this.code = code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(final Language language) {
        this.language = language;
    }

    public Map<Language, String> getNameMap() {
        return nameMap;
    }

    public void setNameMap(final Map<Language, String> nameMap) {
        this.nameMap = nameMap;
    }

    public Map<Language, String> getNoteMap() {
        return noteMap;
    }

    public void setNoteMap(final Map<Language, String> noteMap) {
        this.noteMap = noteMap;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(final String packaging) {
        this.packaging = packaging;
    }

    public String printPrice() {
        return "$" + unitPrice.toString() + "/" + unit.toString();
    }

    // Default to English
    public String getName() {
        return getStringForLanguage(nameMap);
    }

    public String getNote() {
        return getStringForLanguage(noteMap) != null ? getStringForLanguage(noteMap) : "";
    }

    private String getStringForLanguage(final Map<Language, String> map) {
        if (map.get(language) != null && !map.get(language).equals("")) {
            return map.get(language);
        }
        else if (map.get(Language.ENG) != null && !map.get(Language.ENG).equals("")) {
            return map.get(Language.ENG);
        }
        else {
            return "";
        }
    }

    public String printFullDescription() {
        final StringBuilder descBuilder = new StringBuilder(printPrice());

        if (packaging != null && !"".equals(packaging)) {
            descBuilder.append(" ").append(packaging);
        }

        final String note = getNote();
        if (note != null && !note.equals("")) {
            descBuilder.append(" ").append(note);
        }

        return descBuilder.toString();
    }

}
