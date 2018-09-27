package com.example.demo.utils;

/*import com.hisense.rop.common.exception.ServiceException;
import com.hisense.rop.common.model.UserModel;
import com.hisense.rop.common.util.DateUtil;
import com.hisense.rop.common.util.MapUtils;
import com.hisense.rop.common.util.StringUtils;
import com.hisense.rop.model.sys.SysOrg;
import com.hisense.rop.portal.mapper.common.ExSupplierMapper;
import com.hisense.rop.portal.mapper.common.WholesaleCustMapper;
import com.hisense.rop.portal.mapper.plu.BrandMapper;
import com.hisense.rop.portal.mapper.plu.ClsMapper;
import com.hisense.rop.portal.mapper.sys.SysOrgMapper;
import com.hisense.rop.portal.service.common.ICurrentUserInfoService;
import com.hisense.rop.portal.service.sys.GeneratorService;
import com.hisense.rop.portal.service.sys.ISysUserDataOrgService;*/
import com.example.demo.mapper.NovelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Pattern;

/* *
 * @Author tomsun28
 * @Description 高频方法工具类
 * @Date 14:08 2018/3/12
 */
@Component
public class CommonUtil {

  /*  @Autowired
    private GeneratorService generatorService;

    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private ClsMapper clsMapper;

    @Autowired
    private SysOrgMapper sysOrgMapper;//组织机构

    @Autowired
    private ISysUserDataOrgService iSysUserDataOrgService;

    @Autowired
    private ICurrentUserInfoService iCurrentUserInfoService;

    @Autowired
    private WholesaleCustMapper wholesaleCustMapper;

    @Autowired
    private ExSupplierMapper exSupplierMapper;*/

    @Autowired
    private NovelMapper novelMapper;

    public static CommonUtil commonUtil;

    @PostConstruct
    public void init(){
        commonUtil=this;
    }


    /* *
     * @Description 获取指定位数的随机数
     * @Param [length]
     * @Return java.lang.String
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取数据库当前系统时间
     * @return
     */
    public static Date getDatebaseSysDate() throws DataAccessException {
        //获取Mysql当前系统时间
        Date sysDate = commonUtil.novelMapper.getMysqlDate();
        return sysDate;
    }

    //获取code下一位数
  /*  public static String getCodeAdd(String code){
        Long length=Long.valueOf(code)+1;
        int zero=code.length()-length.toString().length();
        if (zero==0){
            code=length.toString();
        }else{
            for (int i=0;i<zero;i++){
                code="0"+length.toString();
            }
        }
        return code;
    }*/



  /*  public static String getNumber(String type,String parentCode){

        String number = "0";
        int levelMax = CacheUtils.getLevelMaxValue(type.toUpperCase()+"_NUMBER",parentCode);
        if(type.equals("brand")){
            number =  commonUtil.brandMapper.getMaxNumber(parentCode);
        }
        if(type.equals("cls")){
            number =  commonUtil.clsMapper.getMaxNumber(parentCode);
        }
        if(type.equals("plu")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("employee")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("contract")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("rng")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("org")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("supplier")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("cnt")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("packet")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("emp")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("role")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("prcPlc")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("warehouse")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("pur_order")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("customer")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("wholesale_cust")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("org_supplier")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("salposparmaitems")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("salPosParmaDefault")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("posarea")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }
        if(type.equals("pos")){
            number =  commonUtil.sysOrgMapper.getMaxNumber(parentCode);
        }



        if((null==number||number=="0"||number.equals("")||"null".equals(number)) && (levelMax!=-1)){//第一个子节点

            String levelMaxValue = "";
            for(int i=0;i<levelMax-1;i++){
                levelMaxValue +="0";
            }
            number = parentCode + levelMaxValue +"1";
        }
        double numbdouble = Math.pow(10,levelMax);
        String numStr = parentCode+String.valueOf(numbdouble-1.0);
        *//*if(Double.parseDouble(number)>=Double.parseDouble(numStr)) {//子节点的最大数
            number = parentCode+""+numStr;
        }*//*

        return number;
    }*/

    /**
     * 生成品牌，品类编码
     * @param dicType   需要查询的字段名 （CLS_CODE或者BRAND_CODE）
     * @param tableName 表名 (CLS或者BRAND)
     * @param parentCodeFiled 父级编码字段名(PARENT_CLS_ID或者PARENT_BRAND_ID)
     * @param pCode   父级编码的值(PARENT_CLS_ID或者PARENT_BRAND_ID的值)
     * @return
     */
   /* public static String getCode(String tableName,String dicType,String parentCodeFiled,String pCode){
        String code="";

        if(pCode==null||"".equals(pCode)){
            if ("CLS".equals(tableName)){
                code=commonUtil.sysOrgMapper.getClsCode(tableName,dicType);
            }else{
                code=commonUtil.sysOrgMapper.getBrandCode(tableName,dicType);
            }

            //说明添加一级编码
            if (!"".equals("code")||code!=null){
                code=getCodeAdd(code);
            }else{
                String firstVal="";
                int firstDicVal=Integer.parseInt(CacheUtils.getDicByVal(dicType.toUpperCase(),"1"));
                for(int i=0;i<firstDicVal-1;i++){
                    firstVal +="0";
                }
                code=firstVal+"1";
            }

            return code;
        }

        //查询数据库表父级编码中对应子级编码的最大值
        code=commonUtil.sysOrgMapper.getMaxCode(tableName.toUpperCase(),dicType.toUpperCase(),parentCodeFiled.toUpperCase(),pCode);


        if (code==null||code.equals("")){
            //说明数据库中没有子级编码
            String filed=tableName.toUpperCase()+"_ID";
            code=commonUtil.sysOrgMapper.getMaxCode(tableName.toUpperCase(),dicType.toUpperCase(),filed,pCode);
            //获取编码需要增加的位数
            int levelMax=CacheUtils.getLevelMaxValue(dicType.toUpperCase(), code);

            if (levelMax==-1){
                throw new ServiceException("当前上级编码为最后一级编码,无法添加");
            }

            String levelMaxValue = "";
            for(int i=0;i<levelMax-1;i++){
                levelMaxValue +="0";
            }
            code = code + levelMaxValue +"1";
        }else{
            code=getCodeAdd(code);
        }
        return code;
    }*/


    /**
     * 获取员工编码
     * @param tableName EMPLOYEE
     * @param dicType EMP_CODE
     * @param parentCodeFiled   ORG_CODE
     * @param pCode ORG_CODE的值
     * @return
     */
  /*  public static String getEmpCode(String tableName,String dicType,String parentCodeFiled,String pCode){
        String code=commonUtil.sysOrgMapper.getCommonCode(tableName,dicType,parentCodeFiled,pCode);
        if ("".equals(code)||code==null){
            String currentDate=DateUtil.getCurrentDate();
            String currentYear=currentDate.substring(2,2);
            code=pCode.substring(pCode.length()-2)+currentYear+"0001";
        }else{
            code=getCodeAdd(code);
        }
        return code;
    }*/

    /**
     * 获取用户组编码
     * @param tableName SYS_USER_ORG
     * @param dicType
     * @param pCode
     * @return
     */
   /* public static String getUserGroupCode(String tableName,String dicType,String parentCodeFiled,String pCode){
        String code=commonUtil.sysOrgMapper.getCommonCode(tableName,dicType,parentCodeFiled,pCode);
        if ("".equals(code)||code==null){
            code=pCode.substring(pCode.length()-2)+"01";
        }else{
            code=getCodeAdd(code);
        }
        return code;
    }*/

    /**
     * 获取供应商编码
     * @param tableName EX_SUPPLIER
     * @param dicType   EX_SUPPLIER_CODE
     * @param parentCodeFiled   ORG_CODE
     * @param pCode     ORG_CODE的值
     * @return
     */
   /* public static String getSupplierCode(String tableName,String dicType,String parentCodeFiled,String pCode){
        String  code=commonUtil.sysOrgMapper.getCommonCode(tableName,dicType,parentCodeFiled,pCode);
        if ("".equals(code)||code==null){
            if (pCode.length()==1){
                //说明只有一位，前面补0
                code="80"+pCode+"0001";
            }else{
                code="8"+pCode.substring(pCode.length()-2)+"0001";
            }
        }else{
            code=getCodeAdd(code);
            //判断编码是否存在,如果存在则加1
            String repeatCode=commonUtil.exSupplierMapper.getRepeatCode("code");
            if (!StringUtils.isEmpty(repeatCode)){
                code=getCodeAdd(repeatCode);
            }
        }
        return code;
    }*/

    /**
     * 获取批发客户编码
     * @param tableName WHOLESALE_CUST
     * @param dicType   WHOLESALE_CUST_CODE
     * @param parentCodeFiled     ORG_CODE
     * @param pCode ORG_CODE的值
     * @return
     */
  /*  public static String getWholeCusCode(String tableName,String dicType,String parentCodeFiled,String pCode){
        String  code=commonUtil.sysOrgMapper.getCommonCode(tableName,dicType,parentCodeFiled,pCode);
        if ("".equals(code)||code==null){
            if (pCode.length()==1){
                //说明只有一位，前面补0
                code="90"+pCode+"0001";
            }else{
                code="9"+pCode.substring(pCode.length()-2)+"0001";
            }
        }else{
            code=getCodeAdd(code);
            //判断编码是否存在,如果存在则加1
            String repeatCode=commonUtil.wholesaleCustMapper.getRepeatCode("code");
            if (!StringUtils.isEmpty(repeatCode)){
                code=getCodeAdd(repeatCode);
            }

        }
        return code;
    }*/

    /**
     * 获取采购合同编码
     * @param tableName CNT
     * @param dicType   CNT_CODE
     * @param parentCodeFiled   供应商ID
     * @param pId 供应商ID的值
     * @param pCode 供应商编码的值
     * @return
     */
   /* public static String getContractCode(String tableName,String dicType,String parentCodeFiled,String pId,String pCode){
        String  code=commonUtil.sysOrgMapper.getCommonCode(tableName,dicType,parentCodeFiled,pId);
        if ("".equals(code)||code==null){
            code=pCode+"01";
        }else{
            code=getCodeAdd(code);
        }
        return code;
    }*/

    /**
     * 获取商品编码
     * @param tableName PLU
     * @param dicType   PLU_CODE
     * @param parentCodeFiled   NET_CLS_CODE或者SHOP_CLS_CODE
     * @param pCode NET_CLS_CODE或者SHOP_CLS_CODE的值
     * @return
     */
   /* public static String getPlusCode(String tableName,String dicType,String parentCodeFiled,String pCode){
        String code = "";
        //如果品类编码长度不小于6位,取前6位作为商品编码前缀
        if (pCode.length() >= 6) {
            pCode = pCode.substring(0,6);
        }

        code=commonUtil.sysOrgMapper.getCommonCodeByLikePCode(tableName, dicType, parentCodeFiled, pCode);

        //品类编码不足6位,自动补0
        if (pCode.length() < 6) {
            for (int i=0; i<6; i++) {
                if (i >= pCode.length()) {
                    pCode += "0";
                }
            }
        }

        if (StringUtils.isEmpty(code)){
            code = pCode +"00001";
        }else{
            code = getCodeAdd(code);
        }

        return code;
    }*/

    /**
     * 获取总部仓库编码
     * @param tableName WAREHOUSE
     * @param dicType   WH_CODE
     * @param parentCodeFiled   ORG_CODE
     * @param pCode ORG_CODE的值
     * @return
     */
  /*  public static String getTotalWarehouseCode(String tableName,String dicType,String parentCodeFiled,String pCode){
        String  code=commonUtil.sysOrgMapper.getCommonCodeWithoutPcode(tableName,dicType,parentCodeFiled,pCode);
        if ("".equals(code)||code==null){
            code="001";
        }else{
            code=getCodeAdd(code);
        }
        return code;
    }*/

    /**
     * 获取分公司仓库编码
     * @param tableName WAREHOUSE
     * @param dicType   WH_CODE
     * @param parentCodeFiled   ORG_CODE
     * @param pCode     ORG_CODE的值
     * @return
     */
  /*  public static String getBranchWarehouseCode(String tableName,String dicType,String parentCodeFiled,String pCode){
        String  code=commonUtil.sysOrgMapper.getCommonCodeWithoutPcode(tableName,dicType,parentCodeFiled,pCode);
        if ("".equals(code)||code==null){
            code=pCode+"001";
        }else{
            code=getCodeAdd(code);
        }
        return code;
    }*/

    /**
     * 获取门店仓库编码
     * @param tableName WAREHOUSE
     * @param dicType   WH_CODE
     * @param parentCodeFiled   ORG_CODE
     * @param pCode ORG_CODE的值
     * @return
     */
  /*  public static String getShopWarehouseCode(String tableName,String dicType,String parentCodeFiled,String pCode){
        String  code=commonUtil.sysOrgMapper.getCommonCodeWithoutPcode(tableName,dicType,parentCodeFiled,pCode);
        if ("".equals(code)||code==null){
            code=pCode+"01";
        }else{
            code=getCodeAdd(code);
        }
        return code;
    }*/

    /**
     * 获取集中定价政策编码
     * @param tableName     PRC_PLC
     * @param dicType       PRC_PLC_CODE
     * @param parentCodeFiled   PRC_ORG_CODE
     * @param pCode     PRC_ORG_CODE的值
     * @return
     */
  /*  public static String getPrcPlcCode(String tableName,String dicType,String parentCodeFiled,String pCode){
        String  code=commonUtil.sysOrgMapper.getCommonCode(tableName,dicType,parentCodeFiled,pCode);
        if ("".equals(code)||code==null){
            code=pCode+"001";
        }else{
            code=getCodeAdd(code);
        }
        return code;
    }*/

    /**
     * 获取采购合同普通费用编码
     * @param tableName     CNT_COST
     * @param dicType       BILL_NO
     * @param parentCodeFiled   CNT_CODE
     * @param pCode     CNT_CODE的值
     * @return
     */
   /* public static String getBillCode(String tableName,String dicType,String parentCodeFiled,String pCode){
        String  code=commonUtil.sysOrgMapper.getCommonCode(tableName,dicType,parentCodeFiled,pCode);
        if ("".equals(code)||code==null){
            code=pCode+"001";
        }else{
            code=getCodeAdd(code);
        }
        return code;
    }*/

    /**
     * 获取角色编码
     * @param tableName     SYS_ROLE
     * @param dicType       ROLE_CODE
     * @param parentCodeFiled   ORG_CODE
     * @param pCode     ORG_CODE的值
     * @return
     */
  /*  public static String getRoleCode(String tableName,String dicType,String parentCodeFiled,String pCode){
        String  code=commonUtil.sysOrgMapper.getCommonCode(tableName,dicType,parentCodeFiled,pCode);
        if ("".equals(code)||code==null){
            code=pCode+"01";
        }else{
            code=getCodeAdd(code);
        }
        return code;
    }*/

    /**
     *
     * @param code       需要校验的编码
     * @param dicType   字段名称
     * @param dicName   编码等级
     * @return  true : 校验通过  false : 校验失败
     */
   /* public static boolean isCorrectRules(String code,String dicType,String dicName){
        String reg=CacheUtils.getRegExp(dicType,dicName);
        Pattern pattern=Pattern.compile(reg);
        return pattern.matcher(code).matches();
    }*/
    /*****
     * 根据当前登录组织来确定数据查询范围
     * @return
     */
   /* public static List<Long> getLimitedDataRangeByOrg(){

        List<Long> orgIdList = new ArrayList<>();

        UserModel userModel = commonUtil.iCurrentUserInfoService.getCurrentUserModel();
        Long orgId = userModel.getOrgId();
        Long userId = userModel.getUserId();

        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("user_id", userId);

        List<Map> userDataOrgList = commonUtil.iSysUserDataOrgService.sysUserDataOrgList(paraMap);
        if(userDataOrgList != null && userDataOrgList.size()>0)
        {
            for(Map dataMap : userDataOrgList)
            {
                String rangeType = MapUtils.getMapKeyVal(dataMap,"range_type");
                String org_id = MapUtils.getMapKeyVal(dataMap,"org_id");
                String org_code = MapUtils.getMapKeyVal(dataMap,"org_code");

                Map map = new HashMap();
                map.put("pOrgCode",org_code);
                map.put("privFlag","1");
                if ("0".equals(rangeType)) //本级
                {
                    orgIdList.add(Long.valueOf(org_id));
                } else if ("1".equals(rangeType))//本级及下级
                {
                    Set<SysOrg> sysOrgSet = CacheUtils.getChildNodes(map);
                    for (SysOrg sysOrg : sysOrgSet) {
                        orgIdList.add(sysOrg.getOrgId());
                    }
                }
            }
        }
        orgIdList.add(orgId);
        return orgIdList;
    }*/
}
