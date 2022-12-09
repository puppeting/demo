package com.inclusive.finance.room;

public class Standard extends MyObject {
    public int id; //
    public Boolean isClick;
    public String BaseCreateTime;
    public String BaseModifyTime;
    public String BaseCreatorId;
    public String BaseModifierId;
    public String Name;//文献名称
    public String Status;// 状态  1修订  2废止 3总后废止 4总装废止  5有效  6有效新版本   7有效老版本   8科工局废止  9限用
    public String StartTime;//姓名
//    public String DepartmentId;//部门id
//    public String DepartmentName;//部门名称
    public String ReleaseTime;// 发布时间
    public String TypeNum;//分类号
    public String RatifyDepartmentId;//批准单位id
    public String RatifyDepartmentName;//批准单位名称

    public String StartDepartmentId;//实施单位id
    public String StartDepartmentName;//实施单位名称
    public String EditorDepartmentId;//主编单位
    public String EditorDepartmentName;//主编单位
    public String Drafter;//起草人
    public String Referencesd;//饮用比按
    public String Standards;//采用标准  多个|分割
    public String FilePath;
    public String Remark;
    public String CollectType;//0位收藏 1已收藏

    public String BiaoNo;//标准号
     public String TypeName;//分类名称
    public String TiChuDepartmentName;//提出单位
    public String SourceId;//标准来源
    public String SourceName;//标准来源名称
    public String Pages;//页数
    public String SameClassNo;//同一分类号标准
    public String ReplaceNo;//代替好
    public String ByReplaceNo;//被代替号
    public String BzKeywords;//关键字
    public String CagegoryId;//标准体系分类id
    public String CagegoryName;//标准体系分类id
    public String SameTopicNo;//同一专题标准

    public String getBzKeywords() {
        return BzKeywords;
    }

    public void setBzKeywords(String bzKeywords) {
        BzKeywords = bzKeywords;
    }

    public String getBiaoNo() {
        return BiaoNo;
    }

    public void setBiaoNo(String biaoNo) {
        BiaoNo = biaoNo;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public String getTiChuDepartmentName() {
        return TiChuDepartmentName;
    }

    public void setTiChuDepartmentName(String tiChuDepartmentName) {
        TiChuDepartmentName = tiChuDepartmentName;
    }

    public String getSourceId() {
        return SourceId;
    }

    public void setSourceId(String sourceId) {
        SourceId = sourceId;
    }

    public String getSourceName() {
        return SourceName;
    }

    public void setSourceName(String sourceName) {
        SourceName = sourceName;
    }

    public String getPages() {
        return Pages;
    }

    public void setPages(String pages) {
        Pages = pages;
    }

    public String getSameClassNo() {
        return SameClassNo;
    }

    public void setSameClassNo(String sameClassNo) {
        SameClassNo = sameClassNo;
    }

    public String getReplaceNo() {
        return ReplaceNo;
    }

    public void setReplaceNo(String replaceNo) {
        ReplaceNo = replaceNo;
    }

    public String getByReplaceNo() {
        return ByReplaceNo;
    }

    public void setByReplaceNo(String byReplaceNo) {
        ByReplaceNo = byReplaceNo;
    }


    public String getCagegoryId() {
        return CagegoryId;
    }

    public void setCagegoryId(String cagegoryId) {
        CagegoryId = cagegoryId;
    }

    public String getCagegoryName() {
        return CagegoryName;
    }

    public void setCagegoryName(String cagegoryName) {
        CagegoryName = cagegoryName;
    }

    public String getSameTopicNo() {
        return SameTopicNo;
    }

    public void setSameTopicNo(String sameTopicNo) {
        SameTopicNo = sameTopicNo;
    }

    public Standard() {
    }

    public Standard(String collectType) {
        CollectType = collectType;
    }

    public String getCollectType() {
        return CollectType;
    }

    public void setCollectType(String collectType) {
        CollectType = collectType;
    }

    public Boolean getClick() {
        return isClick;
    }

    public void setClick(Boolean click) {
        isClick = click;
    }

    public String getTypeNum() {
        return TypeNum;
    }

    public void setTypeNum(String typeNum) {
        TypeNum = typeNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBaseCreateTime() {
        return BaseCreateTime;
    }

    public void setBaseCreateTime(String baseCreateTime) {
        this.BaseCreateTime = baseCreateTime;
    }

    public String getBaseModifyTime() {
        return BaseModifyTime;
    }

    public void setBaseModifyTime(String baseModifyTime) {
        this.BaseModifyTime = baseModifyTime;
    }

    public String getBaseCreatorId() {
        return BaseCreatorId;
    }

    public void setBaseCreatorId(String baseCreatorId) {
        this.BaseCreatorId = baseCreatorId;
    }

    public String getBaseModifierId() {
        return BaseModifierId;
    }

    public void setBaseModifierId(String baseModifierId) {
        this.BaseModifierId = baseModifierId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        this.StartTime = startTime;
    }



    public String getReleaseTime() {
        return ReleaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.ReleaseTime = releaseTime;
    }

//    public String getClassNom() {
//        return ClassNom;
//    }
//
//    public void setClassNom(String classNom) {
//        ClassNom = classNom;
//    }
//    public String getClassNo() {
//        return ClassNo;
//    }
//
//    public void setClassNo(String classNo) {
//        this.ClassNo = classNo;
//    }

    public String getRatifyDepartmentId() {
        return RatifyDepartmentId;
    }

    public void setRatifyDepartmentId(String ratifyDepartmentId) {
        this.RatifyDepartmentId = ratifyDepartmentId;
    }

    public String getRatifyDepartmentName() {
        return RatifyDepartmentName;
    }

    public void setRatifyDepartmentName(String ratifyDepartmentName) {
        this.RatifyDepartmentName = ratifyDepartmentName;
    }

    public String getStartDepartmentId() {
        return StartDepartmentId;
    }

    public void setStartDepartmentId(String startDepartmentId) {
        this.StartDepartmentId = startDepartmentId;
    }

    public String getStartDepartmentName() {
        return StartDepartmentName;
    }

    public void setStartDepartmentName(String startDepartmentName) {
        this.StartDepartmentName = startDepartmentName;
    }

    public String getEditorDepartmentId() {
        return EditorDepartmentId;
    }

    public void setEditorDepartmentId(String editorDepartmentId) {
        this. EditorDepartmentId = editorDepartmentId;
    }

    public String getEditorDepartmentName() {
        return EditorDepartmentName;
    }

    public void setEditorDepartmentName(String editorDepartmentName) {
        this.EditorDepartmentName = editorDepartmentName;
    }

    public String getDrafter() {
        return Drafter;
    }

    public void setDrafter(String drafter) {
        this.Drafter = drafter;
    }

    public String getReferencesd() {
        return Referencesd;
    }

    public void setReferencesd(String referencesd) {
        this.Referencesd = referencesd;
    }

    public String getStandards() {
        return Standards;
    }

    public void setStandards(String standards) {
        Standards = standards;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        this.FilePath = filePath;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        this.Remark = remark;
    }

    public Standard(int id, Boolean isClick, String baseCreateTime, String baseModifyTime, String baseCreatorId, String baseModifierId, String name, String status, String startTime, String releaseTime, String typeNum, String ratifyDepartmentId, String ratifyDepartmentName, String startDepartmentId, String startDepartmentName, String editorDepartmentId, String editorDepartmentName, String drafter, String referencesd, String standards, String filePath, String remark, String collectType, String biaoNo, String typeName, String tiChuDepartmentName, String sourceId, String sourceName, String pages, String sameClassNo, String replaceNo, String byReplaceNo, String bzKeywords, String cagegoryId, String cagegoryName, String sameTopicNo) {
        this.id = id;
        this.isClick = isClick;
        BaseCreateTime = baseCreateTime;
        BaseModifyTime = baseModifyTime;
        BaseCreatorId = baseCreatorId;
        BaseModifierId = baseModifierId;
        Name = name;
        Status = status;
        StartTime = startTime;
        ReleaseTime = releaseTime;
        TypeNum = typeNum;
        RatifyDepartmentId = ratifyDepartmentId;
        RatifyDepartmentName = ratifyDepartmentName;
        StartDepartmentId = startDepartmentId;
        StartDepartmentName = startDepartmentName;
        EditorDepartmentId = editorDepartmentId;
        EditorDepartmentName = editorDepartmentName;
        Drafter = drafter;
        Referencesd = referencesd;
        Standards = standards;
        FilePath = filePath;
        Remark = remark;
        CollectType = collectType;
        BiaoNo = biaoNo;
        TypeName = typeName;
        TiChuDepartmentName = tiChuDepartmentName;
        SourceId = sourceId;
        SourceName = sourceName;
        Pages = pages;
        SameClassNo = sameClassNo;
        ReplaceNo = replaceNo;
        ByReplaceNo = byReplaceNo;
        BzKeywords = bzKeywords;
        CagegoryId = cagegoryId;
        CagegoryName = cagegoryName;
        SameTopicNo = sameTopicNo;
    }
}
