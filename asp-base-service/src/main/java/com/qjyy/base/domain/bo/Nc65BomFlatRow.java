package com.qjyy.base.domain.bo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Nc65BomFlatRow {

    // ===== hdr =====
    private String hdrId;
    private String hdrMlId;
    private String hdrUnit;
    private BigDecimal hdrNnum;
    private String hdrNastunit;
    private BigDecimal hdrNastnum;
    private String hdrRate;
    private String hdrVersion;
    private Integer hdrHbdefault;

    // ===== body =====
    private String bodyId;
    private String bodyParentBomId;
    private String bodyMlId;
    private Integer bodyVersion;
    private String bodyUnit;
    private BigDecimal bodyBaseNum;
    private String bodyNastunit;
    private BigDecimal bodyUseNum;
    private String bodyConvertRate;
    private String bodyBcanreplace;
    private Integer bodyFreplacetype;

    // ===== repl =====
    private String replId;
    private String replCbomBid;
    private String replRowno;
    private String replWlcode;
    private String replWlname;
    private BigDecimal replVreplaceindex;
}
