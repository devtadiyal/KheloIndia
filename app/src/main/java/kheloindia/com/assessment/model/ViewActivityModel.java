package kheloindia.com.assessment.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CT13 on 2017-12-04.
 */

public class ViewActivityModel {


    /**
     * IsSuccess : true
     * Message : Success
     * Others : [{"TestID":"1","TestName":"Assessment"},{"TestID":"2","TestName":"Sports Day Practice"},{"TestID":"3","TestName":"School Annual Day"},{"TestID":"4","TestName":"Substitution Classes"},{"TestID":"5","TestName":"Bad Weather"},{"TestID":"6","TestName":"School Day"},{"TestID":"7","TestName":"PTM"},{"TestID":"9","TestName":"Others"}]
     * Period : [{"Period":"1"},{"Period":"2"},{"Period":"3"},{"Period":"4"},{"Period":"5"},{"Period":"6"},{"Period":"7"},{"Period":"8"},{"Period":"9"},{"Period":"10"},{"Period":"11"}]
     * S365 : [{"TestID":"3","TestName":"Bridge"},{"TestID":"6","TestName":"Hurdle Jump(50m Hurdle Relay Race)"},{"TestID":"8","TestName":"Dance to My Tunes( Twisting and Turning with Music)"},{"TestID":"9","TestName":"In The Rhythm (Skipping and Dancing with Music)"},{"TestID":"33","TestName":"Find That Space"},{"TestID":"38","TestName":"Zig-Zag Running"},{"TestID":"39","TestName":"Turning and Twisting"},{"TestID":"40","TestName":"Balance"},{"TestID":"41","TestName":"Kicking (With inside   of the Foot) "},{"TestID":"42","TestName":"Rolling"},{"TestID":"43","TestName":"Receiving (With inside of the Foot)"},{"TestID":"45","TestName":"Zig-Zag Running"},{"TestID":"46","TestName":"Reflexes"},{"TestID":"47","TestName":"Coordination with Team Members"},{"TestID":"49","TestName":"Attacking"},{"TestID":"51","TestName":"Chasing"},{"TestID":"61","TestName":"30m Bean Bag Race"},{"TestID":"73","TestName":"Cholestrol Knowledge"},{"TestID":"75","TestName":"Healthy Eating"},{"TestID":"120","TestName":"30m Fast Sprint"},{"TestID":"125","TestName":"Ball and Ribbon Drills"},{"TestID":"127","TestName":"Back and Front Roll"},{"TestID":"129","TestName":"Jumping and Crawling Drills"},{"TestID":"130","TestName":"Yoga-Session"},{"TestID":"138","TestName":"Basic Rules"},{"TestID":"177","TestName":"Manipulative Skills-Bouncing/Dribbling"},{"TestID":"178","TestName":"Manipulative Skills-Rolling"},{"TestID":"179","TestName":"Manipulative Skills-Trapping"},{"TestID":"180","TestName":"Manipulative Skills-Punting"},{"TestID":"181","TestName":"Manipulative Skills-Kicking"},{"TestID":"182","TestName":"Manipulative Skills-Volleying"},{"TestID":"183","TestName":"Basic Rules"}]
     * StuGrade : [{"Grade":"Exemplary","GradeId":"1"},{"Grade":"Proficient","GradeId":"2"},{"Grade":"Learning","GradeId":"3"},{"Grade":"Absent","GradeId":"4"}]
     * StudentClasses : [{"Class":"KG-A","ClassID":"C"},{"Class":"KG-B","ClassID":"C"},{"Class":"KG-C","ClassID":"C"},{"Class":"NUR-A","ClassID":"B"},{"Class":"NUR-B","ClassID":"B"},{"Class":"I-A","ClassID":"1"},{"Class":"I-B","ClassID":"1"},{"Class":"I-C","ClassID":"1"},{"Class":"I-D","ClassID":"1"},{"Class":"II-A","ClassID":"2"},{"Class":"II-B","ClassID":"2"},{"Class":"II-C","ClassID":"2"},{"Class":"II-D","ClassID":"2"},{"Class":"III-A","ClassID":"3"},{"Class":"III-B","ClassID":"3"},{"Class":"III-C","ClassID":"3"},{"Class":"III-D","ClassID":"3"},{"Class":"IV-A","ClassID":"4"},{"Class":"IV-C","ClassID":"4"},{"Class":"V-A","ClassID":"5"},{"Class":"V-B","ClassID":"5"},{"Class":"V-C","ClassID":"5"},{"Class":"II-A","ClassID":"2"},{"Class":"II-B","ClassID":"2"},{"Class":"II-C","ClassID":"2"},{"Class":"II-D","ClassID":"2"},{"Class":"III-A","ClassID":"3"},{"Class":"III-B","ClassID":"3"},{"Class":"III-C","ClassID":"3"},{"Class":"III-D","ClassID":"3"},{"Class":"IV-A","ClassID":"4"},{"Class":"IV-B","ClassID":"4"},{"Class":"IV-C","ClassID":"4"},{"Class":"IV-D","ClassID":"4"},{"Class":"IV-E","ClassID":"4"},{"Class":"V-A","ClassID":"5"},{"Class":"V-B","ClassID":"5"},{"Class":"V-C","ClassID":"5"},{"Class":"V-D","ClassID":"5"},{"Class":"V-E","ClassID":"5"},{"Class":"EW-A","ClassID":"B"},{"Class":"EW-B","ClassID":"B"},{"Class":"EW-C","ClassID":"B"},{"Class":"RTF-A","ClassID":"C"},{"Class":"RTF-B","ClassID":"C"},{"Class":"RTF-C","ClassID":"C"},{"Class":"G-1A","ClassID":"1"},{"Class":"G-2A","ClassID":"2"},{"Class":"G-3","ClassID":"3"},{"Class":"G-4","ClassID":"4"},{"Class":"G-5","ClassID":"5"},{"Class":"KG-A","ClassID":"C"},{"Class":"KG-B","ClassID":"C"},{"Class":"Nursery-A","ClassID":"B"},{"Class":"Nursery-B","ClassID":"B"},{"Class":"I-A","ClassID":"1"},{"Class":"I-B","ClassID":"1"},{"Class":"CBSE i - I","ClassID":"1"},{"Class":"CBSE i - II","ClassID":"2"},{"Class":"CBSE i - III","ClassID":"3"},{"Class":"CBSE i - IV","ClassID":"4"},{"Class":"Nur-A","ClassID":"B"},{"Class":"Nur-B","ClassID":"B"},{"Class":"Nur-C","ClassID":"B"},{"Class":"Nur-D","ClassID":"B"},{"Class":"Nur-E","ClassID":"B"},{"Class":"Nur-F","ClassID":"B"},{"Class":"KG-A","ClassID":"C"},{"Class":"KG-B","ClassID":"C"},{"Class":"KG-C","ClassID":"C"},{"Class":"KG-D","ClassID":"C"},{"Class":"KG-E","ClassID":"C"},{"Class":"KG-F","ClassID":"C"},{"Class":"KG-G","ClassID":"C"},{"Class":"P1-A","ClassID":"1"},{"Class":"P1-B","ClassID":"1"},{"Class":"P1-C","ClassID":"1"},{"Class":"P1-D","ClassID":"1"},{"Class":"P1-E","ClassID":"1"},{"Class":"P1-F","ClassID":"1"},{"Class":"P1-G","ClassID":"1"},{"Class":"P1-H","ClassID":"1"},{"Class":"P2-A","ClassID":"2"},{"Class":"P2-B","ClassID":"2"},{"Class":"P2-C","ClassID":"2"},{"Class":"P2-D","ClassID":"2"},{"Class":"P2-E","ClassID":"2"},{"Class":"P2-F","ClassID":"2"},{"Class":"P2-G","ClassID":"2"},{"Class":"P2-H","ClassID":"2"},{"Class":"P3-A","ClassID":"3"},{"Class":"P3-B","ClassID":"3"},{"Class":"P3-C","ClassID":"3"},{"Class":"P3-D","ClassID":"3"},{"Class":"P3-E","ClassID":"3"},{"Class":"P3-F","ClassID":"3"},{"Class":"P3-G","ClassID":"3"},{"Class":"P3-H","ClassID":"3"},{"Class":"P4-A","ClassID":"4"},{"Class":"P4-B","ClassID":"4"},{"Class":"P4-C","ClassID":"4"},{"Class":"P4-D","ClassID":"4"},{"Class":"P4-E","ClassID":"4"},{"Class":"P4-F","ClassID":"4"},{"Class":"P4-G","ClassID":"4"},{"Class":"P4-H","ClassID":"4"},{"Class":"P5-A","ClassID":"5"},{"Class":"P5-B","ClassID":"5"},{"Class":"P5-C","ClassID":"5"},{"Class":"P5-D","ClassID":"5"},{"Class":"P5-E","ClassID":"5"},{"Class":"P5-F","ClassID":"5"},{"Class":"P5-G","ClassID":"5"},{"Class":"P5-H","ClassID":"5"},{"Class":"CBSE i- V","ClassID":"5"},{"Class":"PreNursery","ClassID":"B"},{"Class":"I-A","ClassID":"1"},{"Class":"I-B","ClassID":"1"},{"Class":"I-C","ClassID":"1"},{"Class":"I-D","ClassID":"1"},{"Class":"II-A","ClassID":"2"},{"Class":"II-C","ClassID":"2"},{"Class":"II-D","ClassID":"2"},{"Class":"III-A","ClassID":"3"},{"Class":"III-B","ClassID":"3"},{"Class":"III-C","ClassID":"3"},{"Class":"III-D","ClassID":"3"},{"Class":"III-E","ClassID":"3"},{"Class":"IV-B","ClassID":"4"},{"Class":"IV-D","ClassID":"4"},{"Class":"V-A","ClassID":"5"},{"Class":"V-B","ClassID":"5"},{"Class":"V-C","ClassID":"5"},{"Class":"V-E","ClassID":"5"},{"Class":"VI-A","ClassID":"6"},{"Class":"VI-B","ClassID":"6"},{"Class":"VI-C","ClassID":"6"},{"Class":"VI-D","ClassID":"6"},{"Class":"VII-A","ClassID":"7"},{"Class":"VII-B","ClassID":"7"},{"Class":"VII-D","ClassID":"7"},{"Class":"VIII-A","ClassID":"8"},{"Class":"VIII-B","ClassID":"8"},{"Class":"VIII-C","ClassID":"8"},{"Class":"IX-A","ClassID":"9"},{"Class":"IX-B","ClassID":"9"},{"Class":"IX-C","ClassID":"9"},{"Class":"X-D","ClassID":"10"},{"Class":"X-A","ClassID":"10"},{"Class":"IX-D","ClassID":"9"},{"Class":"X-B","ClassID":"10"},{"Class":"X-C","ClassID":"10"},{"Class":"XI-Arts","ClassID":"11"},{"Class":"XI-Commerce","ClassID":"11"},{"Class":"XI-Science","ClassID":"11"},{"Class":"XII-Commerce","ClassID":"12"},{"Class":"XII-Arts","ClassID":"12"},{"Class":"XII-Science","ClassID":"12"},{"Class":"I-E","ClassID":"1"},{"Class":"II-E","ClassID":"2"},{"Class":"V-D","ClassID":"5"},{"Class":"IV-C","ClassID":"4"},{"Class":"IV-A","ClassID":"4"},{"Class":"IV-E","ClassID":"4"},{"Class":"VIII-D","ClassID":"8"},{"Class":"II-B","ClassID":"2"},{"Class":"VII-C","ClassID":"7"},{"Class":"Pre-Nursery","ClassID":"B"},{"Class":"G-6","ClassID":"6"},{"Class":"VI-A","ClassID":"6"},{"Class":"VI-B","ClassID":"6"},{"Class":"VI-C","ClassID":"6"},{"Class":"VII-A","ClassID":"7"},{"Class":"VII-B","ClassID":"7"},{"Class":"VII-C","ClassID":"7"},{"Class":"VIII-A","ClassID":"8"},{"Class":"VIII-B","ClassID":"8"},{"Class":"G-1B","ClassID":"1"},{"Class":"G-2B","ClassID":"2"},{"Class":"G-3B","ClassID":"3"},{"Class":"G-7","ClassID":"7"},{"Class":"Nursery","ClassID":"B"},{"Class":"I A","ClassID":"1"},{"Class":"I B","ClassID":"1"},{"Class":"I C","ClassID":"1"},{"Class":"NUR-A","ClassID":"B"},{"Class":"I D","ClassID":"1"},{"Class":"I E","ClassID":"1"},{"Class":"I F","ClassID":"1"},{"Class":"I G","ClassID":"1"},{"Class":"I H","ClassID":"1"},{"Class":"II A","ClassID":"2"},{"Class":"II B","ClassID":"2"},{"Class":"II C","ClassID":"2"},{"Class":"NUR-B","ClassID":"B"},{"Class":"II D","ClassID":"2"},{"Class":"II E","ClassID":"2"},{"Class":"II F","ClassID":"2"},{"Class":"II G","ClassID":"2"},{"Class":"II H","ClassID":"2"},{"Class":"III A","ClassID":"3"},{"Class":"III B","ClassID":"3"},{"Class":"III C","ClassID":"3"},{"Class":"LKG-A","ClassID":"C"},{"Class":"III D","ClassID":"3"},{"Class":"III E","ClassID":"3"},{"Class":"III F","ClassID":"3"},{"Class":"III G","ClassID":"3"},{"Class":"III H","ClassID":"3"},{"Class":"IV A","ClassID":"4"},{"Class":"IV B","ClassID":"4"},{"Class":"IV C","ClassID":"4"},{"Class":"IV D","ClassID":"4"},{"Class":"IV E","ClassID":"4"},{"Class":"IV F","ClassID":"4"},{"Class":"IV G","ClassID":"4"},{"Class":"IV H","ClassID":"4"},{"Class":"V A","ClassID":"5"},{"Class":"V B","ClassID":"5"},{"Class":"V C","ClassID":"5"},{"Class":"V D","ClassID":"5"},{"Class":"V E","ClassID":"5"},{"Class":"V F","ClassID":"5"},{"Class":"V G","ClassID":"5"},{"Class":"V H","ClassID":"5"},{"Class":"VI A","ClassID":"6"},{"Class":"VI B","ClassID":"6"},{"Class":"VI C","ClassID":"6"},{"Class":"VI D","ClassID":"6"},{"Class":"VI E","ClassID":"6"},{"Class":"VI F","ClassID":"6"},{"Class":"VI G","ClassID":"6"},{"Class":"VI H","ClassID":"6"},{"Class":"VII A","ClassID":"7"},{"Class":"VII B","ClassID":"7"},{"Class":"VII C","ClassID":"7"},{"Class":"KG","ClassID":"C"},{"Class":"I","ClassID":"1"},{"Class":"II","ClassID":"2"},{"Class":"III","ClassID":"3"},{"Class":"IV","ClassID":"4"},{"Class":"V","ClassID":"5"},{"Class":"V1","ClassID":"6"},{"Class":"LKG-B","ClassID":"C"},{"Class":"VII D","ClassID":"7"},{"Class":"VII E","ClassID":"7"},{"Class":"VII F","ClassID":"7"},{"Class":"VII G","ClassID":"7"},{"Class":"VII H","ClassID":"7"},{"Class":"VIII A","ClassID":"8"},{"Class":"VIII B","ClassID":"8"},{"Class":"VIII C","ClassID":"8"},{"Class":"LKG-C","ClassID":"C"},{"Class":"VIII D","ClassID":"8"},{"Class":"VIII E","ClassID":"8"},{"Class":"VIII F","ClassID":"8"},{"Class":"VIII G","ClassID":"8"},{"Class":"VIII H","ClassID":"8"},{"Class":"IX A","ClassID":"9"},{"Class":"IX B","ClassID":"9"},{"Class":"IX C","ClassID":"9"},{"Class":"LKG-D","ClassID":"C"},{"Class":"IX D","ClassID":"9"},{"Class":"IX E","ClassID":"9"},{"Class":"IX F","ClassID":"9"},{"Class":"IX G","ClassID":"9"},{"Class":"IX H","ClassID":"9"},{"Class":"X A","ClassID":"10"},{"Class":"X B","ClassID":"10"},{"Class":"X C","ClassID":"10"},{"Class":"UKG-A","ClassID":"C"},{"Class":"X D","ClassID":"10"},{"Class":"X E","ClassID":"10"},{"Class":"X F","ClassID":"10"},{"Class":"X G","ClassID":"10"},{"Class":"X H","ClassID":"10"},{"Class":"XI A","ClassID":"11"},{"Class":"XI B","ClassID":"11"},{"Class":"XI C","ClassID":"11"},{"Class":"UKG-B","ClassID":"C"},{"Class":"XI D","ClassID":"11"},{"Class":"XI E","ClassID":"11"},{"Class":"XI F","ClassID":"11"},{"Class":"XI G","ClassID":"11"},{"Class":"XI H","ClassID":"11"},{"Class":"XII A","ClassID":"12"},{"Class":"XII B","ClassID":"12"},{"Class":"XII C","ClassID":"12"},{"Class":"UKG-C","ClassID":"C"},{"Class":"XII D","ClassID":"12"},{"Class":"XII E","ClassID":"12"},{"Class":"XII F","ClassID":"12"},{"Class":"XII G","ClassID":"12"},{"Class":"XII H","ClassID":"12"},{"Class":"NUR-A","ClassID":"B"},{"Class":"NUR-B","ClassID":"B"},{"Class":"PRENUR-A","ClassID":"B"},{"Class":"UKG-D","ClassID":"C"},{"Class":"I-A","ClassID":"1"},{"Class":"I-B","ClassID":"1"},{"Class":"I-C","ClassID":"1"},{"Class":"I-D","ClassID":"1"},{"Class":"II-A","ClassID":"2"},{"Class":"II-B","ClassID":"2"},{"Class":"II-C","ClassID":"2"},{"Class":"II-D","ClassID":"2"},{"Class":"IV-A","ClassID":"4"},{"Class":"IV-B","ClassID":"4"},{"Class":"IV-C","ClassID":"4"},{"Class":"III-A","ClassID":"3"},{"Class":"III-B","ClassID":"3"},{"Class":"III-C","ClassID":"3"},{"Class":"V-A","ClassID":"5"},{"Class":"V-B","ClassID":"5"},{"Class":"V-C","ClassID":"5"},{"Class":"V-D","ClassID":"5"},{"Class":"VI-A","ClassID":"6"},{"Class":"VI-B","ClassID":"6"},{"Class":"VI-C","ClassID":"6"},{"Class":"VI-D","ClassID":"6"},{"Class":"VII-A","ClassID":"7"},{"Class":"VII-B","ClassID":"7"},{"Class":"VII-C","ClassID":"7"},{"Class":"VII-D","ClassID":"7"},{"Class":"VIII-A","ClassID":"8"},{"Class":"VIII-B","ClassID":"8"},{"Class":"VIII-C","ClassID":"8"},{"Class":"VIII-D","ClassID":"8"},{"Class":"IX-A","ClassID":"9"},{"Class":"IX-B","ClassID":"9"},{"Class":"IX-C","ClassID":"9"},{"Class":"X-A","ClassID":"10"},{"Class":"X-B","ClassID":"10"},{"Class":"X-C","ClassID":"10"},{"Class":"XI-A","ClassID":"11"},{"Class":"XI-B","ClassID":"11"},{"Class":"XII-A","ClassID":"12"},{"Class":"XII-B","ClassID":"12"}]
     * TopSports : [{"TestID":"9","TestName":"Athletics"},{"TestID":"13","TestName":"Badminton"},{"TestID":"7","TestName":"Basketball"},{"TestID":"6","TestName":"Cricket"},{"TestID":"3","TestName":"Football"},{"TestID":"10","TestName":"Gymnastics/Aerobics"},{"TestID":"5","TestName":"Handball"},{"TestID":"11","TestName":"Health and Fitness"},{"TestID":"2","TestName":"Kabaddi"},{"TestID":"4","TestName":"Kho Kho"},{"TestID":"12","TestName":"Motor Skills"},{"TestID":"15","TestName":"Squash"},{"TestID":"16","TestName":"TableTennis"},{"TestID":"17","TestName":"Tennis"},{"TestID":"1","TestName":"Throw Ball"},{"TestID":"8","TestName":"Volleyball"}]
     * top_skill : [{"SSkillid":"26","TestID":"6","sskillname":"Back foot batting"},{"SSkillid":"27","TestID":"6","sskillname":"Batting Mechanics"},{"SSkillid":"28","TestID":"6","sskillname":"Catching"},{"SSkillid":"29","TestID":"6","sskillname":"Conditioned games"},{"SSkillid":"30","TestID":"6","sskillname":"Extras Drills"},{"SSkillid":"31","TestID":"6","sskillname":"Fast and spin bowling"},{"SSkillid":"32","TestID":"6","sskillname":"Front foot batting"},{"SSkillid":"33","TestID":"6","sskillname":"Ground fielding and throwing"},{"SSkillid":"34","TestID":"6","sskillname":"Running between the wickets"},{"SSkillid":"35","TestID":"6","sskillname":"Techniques"},{"SSkillid":"36","TestID":"6","sskillname":"Wicket - keeping"}]
     */

    @SerializedName("IsSuccess")
    private String IsSuccess;
    @SerializedName("Message")
    private String Message;
    @SerializedName("Others")
    private List<OthersBean> Others;
    @SerializedName("Period")
    private List<PeriodBean> Period;
    @SerializedName("S365")
    private List<S365Bean> S365;
    @SerializedName("StuGrade")
    private List<StuGradeBean> StuGrade;
    @SerializedName("StudentClasses")
    private List<StudentClassesBean> StudentClasses;
    @SerializedName("TopSports")
    private List<TopSportsBean> TopSports;
    @SerializedName("top_skill")
    private List<TopSkillBean> topSkill;
    @SerializedName("top_sub_skill")
    private List<TopSubSkillBean> topSubSkill;

    public String getIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(String IsSuccess) {
        this.IsSuccess = IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<OthersBean> getOthers() {
        return Others;
    }

    public void setOthers(List<OthersBean> Others) {
        this.Others = Others;
    }

    public List<PeriodBean> getPeriod() {
        return Period;
    }

    public void setPeriod(List<PeriodBean> Period) {
        this.Period = Period;
    }

    public List<S365Bean> getS365() {
        return S365;
    }

    public void setS365(List<S365Bean> S365) {
        this.S365 = S365;
    }

    public List<StuGradeBean> getStuGrade() {
        return StuGrade;
    }

    public void setStuGrade(List<StuGradeBean> StuGrade) {
        this.StuGrade = StuGrade;
    }

    public List<StudentClassesBean> getStudentClasses() {
        return StudentClasses;
    }

    public void setStudentClasses(List<StudentClassesBean> StudentClasses) {
        this.StudentClasses = StudentClasses;
    }

    public List<TopSportsBean> getTopSports() {
        return TopSports;
    }

    public void setTopSports(List<TopSportsBean> TopSports) {
        this.TopSports = TopSports;
    }

    public List<TopSkillBean> getTopSkill() {
        return topSkill;
    }

    public void setTopSkill(List<TopSkillBean> topSkill) {
        this.topSkill = topSkill;
    }

    public List<TopSubSkillBean> getTopSubSkill() {
        return topSubSkill;
    }

    public void setTopSubSkill(List<TopSubSkillBean> topSubSkill) {
        this.topSubSkill = topSubSkill;
    }


    public static class OthersBean {
        /**
         * TestID : 1
         * TestName : Assessment
         */

        @SerializedName("id")
        private String TestID;
        @SerializedName("Activity_name")
        private String TestName;

        public String getTestID() {
            return TestID;
        }

        public void setTestID(String TestID) {
            this.TestID = TestID;
        }

        public String getTestName() {
            return TestName;
        }

        public void setTestName(String TestName) {
            this.TestName = TestName;
        }
    }

    public static class PeriodBean {
        /**
         * Period : 1
         */

        @SerializedName("Period")
        private String Period;

        public String getPeriod() {
            return Period;
        }

        public void setPeriod(String Period) {
            this.Period = Period;
        }
    }

    public static class S365Bean {
        /**
         * TestID : 3
         * TestName : Bridge
         */

        @SerializedName("TestID")
        private String TestID;
        @SerializedName("TestName")
        private String TestName;

        public String getTestID() {
            return TestID;
        }

        public void setTestID(String TestID) {
            this.TestID = TestID;
        }

        public String getTestName() {
            return TestName;
        }

        public void setTestName(String TestName) {
            this.TestName = TestName;
        }
    }

    public static class StuGradeBean {
        /**
         * Grade : Exemplary
         * GradeId : 1
         */

        @SerializedName("Grade")
        private String Grade;
        @SerializedName("GradeId")
        private String GradeId;

        public String getGrade() {
            return Grade;
        }

        public void setGrade(String Grade) {
            this.Grade = Grade;
        }

        public String getGradeId() {
            return GradeId;
        }

        public void setGradeId(String GradeId) {
            this.GradeId = GradeId;
        }
    }

    public static class StudentClassesBean {
        /**
         * Class : KG-A
         * ClassID : C
         */

        @SerializedName("Class")
        private String Class;
        @SerializedName("ClassID")
        private String ClassID;

        public String getClasss() {
            return Class;
        }

        public void setClass(String Class) {
            this.Class = Class;
        }

        public String getClassID() {
            return ClassID;
        }

        public void setClassID(String ClassID) {
            this.ClassID = ClassID;
        }
    }

    public static class TopSportsBean {
        /**
         * TestID : 9
         * TestName : Athletics
         */

        @SerializedName("TestID")
        private String TestID;
        @SerializedName("TestName")
        private String TestName;

        public String getTestID() {
            return TestID;
        }

        public void setTestID(String TestID) {
            this.TestID = TestID;
        }

        public String getTestName() {
            return TestName;
        }

        public void setTestName(String TestName) {
            this.TestName = TestName;
        }
    }

    public static class TopSkillBean {
        /**
         * SSkillid : 26
         * TestID : 6
         * sskillname : Back foot batting
         */

        @SerializedName("SSkillid")
        private String SSkillid;
        @SerializedName("TestID")
        private String TestID;
        @SerializedName("sskillname")
        private String sskillname;

        public String getSSkillid() {
            return SSkillid;
        }

        public void setSSkillid(String SSkillid) {
            this.SSkillid = SSkillid;
        }

        public String getTestID() {
            return TestID;
        }

        public void setTestID(String TestID) {
            this.TestID = TestID;
        }

        public String getSskillname() {
            return sskillname;
        }

        public void setSskillname(String sskillname) {
            this.sskillname = sskillname;
        }
    }


    public static class TopSubSkillBean {
        /**
         * Skill_ID : 68
         * Technique_Name : Forehand grip
         * technique_ID : 226
         */

        @SerializedName("SSkillid")
        private String SkillID;
        @SerializedName("Technique_Name")
        private String TechniqueName;
        @SerializedName("technique_ID")
        private String techniqueID;

        public String getSkillID() {
            return SkillID;
        }

        public void setSkillID(String SkillID) {
            this.SkillID = SkillID;
        }

        public String getTechniqueName() {
            return TechniqueName;
        }

        public void setTechniqueName(String TechniqueName) {
            this.TechniqueName = TechniqueName;
        }

        public String getTechniqueID() {
            return techniqueID;
        }

        public void setTechniqueID(String techniqueID) {
            this.techniqueID = techniqueID;
        }
    }
}
