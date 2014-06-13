/*
============================================================================
文件名称	:	LiturgicYear.cpp
公司		:	CathAssist
作者		:	李亚科
创建时间	:	2014-06-12 12:27
修改时间	:	2014-06-12 12:30
说明		:	用于计算礼仪年
============================================================================
*/

#include "LiturgicYear.h"
#include <sstream>

using namespace CathAssist::Calendar;

std::multimap<int,CellInfo> CathAssist::Calendar::LiturgicYear::mapPropers;

void CathAssist::Calendar::LiturgicYear::initPropers()
{
	//初始化常用节日（日期固定）

	//1月
	mapPropers.insert(std::make_pair(101,CellInfo(SOLEMNITY,WHITE,"天主之母节")));
	mapPropers.insert(std::make_pair(102,CellInfo(MEMORIAL,WHITE,"圣巴西略及圣额我略·纳齐安（主教、圣师)")));
	mapPropers.insert(std::make_pair(103,CellInfo(OPTIONAL,NOCOLOR,"耶稣圣名节")));
	mapPropers.insert(std::make_pair(106,CellInfo(SOLEMNITY,WHITE,"主显节")));
	mapPropers.insert(std::make_pair(107,CellInfo(OPTIONAL,NOCOLOR,"圣雷孟（司铎）")));
	mapPropers.insert(std::make_pair(113,CellInfo(OPTIONAL,NOCOLOR,"圣依拉略（主教、圣师）")));
	mapPropers.insert(std::make_pair(117,CellInfo(MEMORIAL,WHITE,"圣安东尼（院长）")));
	mapPropers.insert(std::make_pair(120,CellInfo(OPTIONAL,NOCOLOR,"圣法比盎（教宗、殉道)")));
	mapPropers.insert(std::make_pair(120,CellInfo(OPTIONAL,NOCOLOR,"圣巴斯弟盎（殉道）")));
	mapPropers.insert(std::make_pair(121,CellInfo(MEMORIAL,RED,"圣依搦斯（童贞、殉道)")));
	mapPropers.insert(std::make_pair(122,CellInfo(OPTIONAL,NOCOLOR,"圣味增爵（执事、殉道）")));
	mapPropers.insert(std::make_pair(124,CellInfo(MEMORIAL,WHITE,"圣方济各·沙雷氏（主教、圣师）")));
	mapPropers.insert(std::make_pair(125,CellInfo(FEAST,WHITE,"圣保禄归化（宗徒）")));
	mapPropers.insert(std::make_pair(126,CellInfo(MEMORIAL,WHITE,"圣弟茂德与圣弟铎（主教）")));
	mapPropers.insert(std::make_pair(127,CellInfo(OPTIONAL,NOCOLOR,"圣安琪拉·美利西（童贞）")));
	mapPropers.insert(std::make_pair(128,CellInfo(MEMORIAL,WHITE,"圣多玛斯·阿奎纳（司铎、圣师）")));
	mapPropers.insert(std::make_pair(131,CellInfo(MEMORIAL,WHITE,"圣若望·鲍思高（司铎）")));

    //2月
	mapPropers.insert(std::make_pair(202,CellInfo(LORD,WHITE,"献主节")));
	mapPropers.insert(std::make_pair(203,CellInfo(OPTIONAL,NOCOLOR,"圣巴拉斯（主教、殉道）")));
	mapPropers.insert(std::make_pair(203,CellInfo(OPTIONAL,NOCOLOR,"圣安斯卡（主教）")));
	mapPropers.insert(std::make_pair(205,CellInfo(MEMORIAL,RED,"圣女亚加大（殉道）")));
	mapPropers.insert(std::make_pair(206,CellInfo(MEMORIAL,RED,"圣保禄三木及同伴（日本殉道者）")));
	mapPropers.insert(std::make_pair(208,CellInfo(OPTIONAL,NOCOLOR,"圣热罗尼莫·艾明廉")));
	mapPropers.insert(std::make_pair(208,CellInfo(OPTIONAL,NOCOLOR,"St. Josephine Margaret Bakhita（童贞）")));
	mapPropers.insert(std::make_pair(210,CellInfo(MEMORIAL,WHITE,"圣思嘉（童贞）")));
	mapPropers.insert(std::make_pair(211,CellInfo(OPTIONAL,NOCOLOR,"露德圣母")));
	mapPropers.insert(std::make_pair(214,CellInfo(MEMORIAL,WHITE,"圣济利禄（隐修士）及圣默多狄（主教）")));
	mapPropers.insert(std::make_pair(217,CellInfo(OPTIONAL,NOCOLOR,"圣母忠仆会七位会祖")));
	mapPropers.insert(std::make_pair(221,CellInfo(OPTIONAL,NOCOLOR,"圣伯多禄·达弥盎（主教、圣师）")));
	mapPropers.insert(std::make_pair(222,CellInfo(FEAST,WHITE,"建立圣伯多禄宗座")));
	mapPropers.insert(std::make_pair(223,CellInfo(MEMORIAL,RED,"圣玻里加（主教、殉道）")));
    
    //3月
	mapPropers.insert(std::make_pair(304,CellInfo(OPTIONAL,NOCOLOR,"圣加西弥禄（殉道）")));
	mapPropers.insert(std::make_pair(307,CellInfo(MEMORIAL,RED,"圣女伯尔都亚及圣女斐尼丝（殉道)")));
	mapPropers.insert(std::make_pair(308,CellInfo(OPTIONAL,NOCOLOR,"圣若望由天主者（会士）")));
	mapPropers.insert(std::make_pair(309,CellInfo(OPTIONAL,NOCOLOR,"圣女方济加·露雯（修女）")));
	mapPropers.insert(std::make_pair(317,CellInfo(OPTIONAL,NOCOLOR,"圣博德（主教）")));
	mapPropers.insert(std::make_pair(318,CellInfo(OPTIONAL,NOCOLOR,"耶路撒冷的圣济利禄（主教、圣师）")));
	mapPropers.insert(std::make_pair(319,CellInfo(SOLEMNITY,WHITE,"大圣若瑟，童贞圣母玛利亚净配")));
	mapPropers.insert(std::make_pair(323,CellInfo(OPTIONAL,NOCOLOR,"圣多利坡（主教）")));
	mapPropers.insert(std::make_pair(325,CellInfo(SOLEMNITY,WHITE,"圣母领报")));
    
    //4月
	mapPropers.insert(std::make_pair(402,CellInfo(OPTIONAL,NOCOLOR,"圣方济各保拉（隐修士）")));
	mapPropers.insert(std::make_pair(404,CellInfo(OPTIONAL,NOCOLOR,"圣依西多禄（主教、圣师）")));
	mapPropers.insert(std::make_pair(405,CellInfo(OPTIONAL,NOCOLOR,"圣味增爵斐洛（司铎）")));
	mapPropers.insert(std::make_pair(407,CellInfo(MEMORIAL,WHITE,"圣若翰·喇沙（司铎）")));
	mapPropers.insert(std::make_pair(411,CellInfo(MEMORIAL,RED,"圣达尼老（主教、殉道）")));
	mapPropers.insert(std::make_pair(413,CellInfo(OPTIONAL,NOCOLOR,"圣玛尔定一世（教宗、殉道）")));
	mapPropers.insert(std::make_pair(421,CellInfo(OPTIONAL,NOCOLOR,"圣安瑟莫（主教、圣师）")));
	mapPropers.insert(std::make_pair(423,CellInfo(OPTIONAL,NOCOLOR,"圣乔治（殉道）；或布拉格的圣道博")));
	mapPropers.insert(std::make_pair(424,CellInfo(OPTIONAL,NOCOLOR,"圣斐德理（司铎、殉道）")));
	mapPropers.insert(std::make_pair(425,CellInfo(FEAST,RED,"圣马尔谷（圣史）")));
	mapPropers.insert(std::make_pair(428,CellInfo(OPTIONAL,NOCOLOR,"圣伯多禄·查纳（司铎、殉道）")));
	mapPropers.insert(std::make_pair(428,CellInfo(OPTIONAL,NOCOLOR,"圣路易·蒙福（司铎）")));
	mapPropers.insert(std::make_pair(429,CellInfo(MEMORIAL,WHITE,"圣女加大利纳（贞女、圣师）")));
	mapPropers.insert(std::make_pair(430,CellInfo(OPTIONAL,NOCOLOR,"圣庇护五世（教宗）")));
    
    //5月
	mapPropers.insert(std::make_pair(501,CellInfo(OPTIONAL,NOCOLOR,"圣若瑟劳工主保")));
	mapPropers.insert(std::make_pair(502,CellInfo(MEMORIAL,WHITE,"圣亚大纳修（主教、圣师）")));
	mapPropers.insert(std::make_pair(503,CellInfo(FEAST,RED,"圣斐理伯与圣雅各伯（宗徒）")));
	mapPropers.insert(std::make_pair(512,CellInfo(OPTIONAL,NOCOLOR,"圣庞加爵（殉道）")));
	mapPropers.insert(std::make_pair(512,CellInfo(OPTIONAL,NOCOLOR,"圣聂勒及圣亚基略（殉道）")));
	mapPropers.insert(std::make_pair(513,CellInfo(OPTIONAL,NOCOLOR,"花地玛圣母")));
	mapPropers.insert(std::make_pair(514,CellInfo(FEAST,RED,"圣玛弟亚（宗徒）")));
	mapPropers.insert(std::make_pair(518,CellInfo(OPTIONAL,NOCOLOR,"圣若望一世（教宗、殉道）")));
	mapPropers.insert(std::make_pair(520,CellInfo(OPTIONAL,NOCOLOR,"圣伯尔纳定（司铎）")));
	mapPropers.insert(std::make_pair(521,CellInfo(OPTIONAL,NOCOLOR,"圣多福·麦哲伦（司铎）及其他殉道者（殉道）")));
	mapPropers.insert(std::make_pair(522,CellInfo(OPTIONAL,NOCOLOR,"圣李达（绝望主保）")));
	mapPropers.insert(std::make_pair(525,CellInfo(OPTIONAL,NOCOLOR,"圣伯达（司铎、圣师）")));
	mapPropers.insert(std::make_pair(525,CellInfo(OPTIONAL,NOCOLOR,"圣额我略七世（教宗）")));
	mapPropers.insert(std::make_pair(525,CellInfo(OPTIONAL,NOCOLOR,"圣玛达肋纳·巴斯（童贞）")));
	mapPropers.insert(std::make_pair(526,CellInfo(MEMORIAL,WHITE,"圣斐理伯·内利（司铎）")));
	mapPropers.insert(std::make_pair(527,CellInfo(OPTIONAL,NOCOLOR,"坎特伯雷的圣奥思定（主教）")));
	mapPropers.insert(std::make_pair(531,CellInfo(FEAST,WHITE,"圣母访亲")));
    
    //6月
	mapPropers.insert(std::make_pair(601,CellInfo(MEMORIAL,RED,"圣犹思定（殉道）")));
	mapPropers.insert(std::make_pair(602,CellInfo(OPTIONAL,NOCOLOR,"圣玛策林及圣伯多禄（殉道）")));
	mapPropers.insert(std::make_pair(603,CellInfo(MEMORIAL,RED,"圣嘉禄·安加及同伴（乌干达殉道者）")));
	mapPropers.insert(std::make_pair(605,CellInfo(MEMORIAL,RED,"圣玻尼法（主教、殉道）")));
	mapPropers.insert(std::make_pair(606,CellInfo(OPTIONAL,NOCOLOR,"圣诺伯多（主教）")));
	mapPropers.insert(std::make_pair(609,CellInfo(OPTIONAL,NOCOLOR,"圣义范（执事、圣师）")));
	mapPropers.insert(std::make_pair(611,CellInfo(MEMORIAL,RED,"圣巴尔纳伯（宗徒）")));
	mapPropers.insert(std::make_pair(613,CellInfo(MEMORIAL,WHITE,"圣安多尼·巴都亚（司铎、圣师）")));
	mapPropers.insert(std::make_pair(619,CellInfo(OPTIONAL,NOCOLOR,"圣罗慕德（院长）")));
	mapPropers.insert(std::make_pair(621,CellInfo(MEMORIAL,WHITE,"圣类思·公撒格（会士）")));
	mapPropers.insert(std::make_pair(622,CellInfo(OPTIONAL,NOCOLOR,"圣保林（主教）")));
	mapPropers.insert(std::make_pair(622,CellInfo(OPTIONAL,NOCOLOR,"圣若望·费舍（主教、殉道）及圣托马斯·莫尔（殉道）")));
	mapPropers.insert(std::make_pair(624,CellInfo(SOLEMNITY,WHITE,"圣若翰洗者诞生")));
	mapPropers.insert(std::make_pair(627,CellInfo(OPTIONAL,NOCOLOR,"圣济利禄（主教、圣师）")));
	mapPropers.insert(std::make_pair(628,CellInfo(MEMORIAL,RED,"圣爱任纽（主教、殉道）")));
	mapPropers.insert(std::make_pair(629,CellInfo(SOLEMNITY,RED,"圣伯多禄及圣保禄（宗徒）")));
	mapPropers.insert(std::make_pair(630,CellInfo(OPTIONAL,NOCOLOR,"罗马教会初期殉道烈士")));
    
    //7月
    mapPropers.insert(std::make_pair(703,CellInfo(FEAST,RED,"圣多默（宗徒）")));
    mapPropers.insert(std::make_pair(704,CellInfo(OPTIONAL,NOCOLOR,"葡萄牙的圣依撒伯尔")));
    mapPropers.insert(std::make_pair(705,CellInfo(OPTIONAL,NOCOLOR,"圣安多尼·匝加利（司铎）")));
    mapPropers.insert(std::make_pair(706,CellInfo(OPTIONAL,NOCOLOR,"圣玛利亚·葛莱蒂（童贞、殉道）")));
    mapPropers.insert(std::make_pair(709,CellInfo(OPTIONAL,NOCOLOR,"中华诸圣及圣赵荣思定神父（司铎、殉道）")));
    mapPropers.insert(std::make_pair(711,CellInfo(MEMORIAL,WHITE,"圣本笃（院长）")));
    mapPropers.insert(std::make_pair(713,CellInfo(OPTIONAL,NOCOLOR,"圣皇亨利")));
    mapPropers.insert(std::make_pair(714,CellInfo(OPTIONAL,NOCOLOR,"圣加弥禄·弥理（司铎）")));
    mapPropers.insert(std::make_pair(715,CellInfo(MEMORIAL,WHITE,"圣文德（主教、圣师）")));
    mapPropers.insert(std::make_pair(716,CellInfo(OPTIONAL,NOCOLOR,"加尔默罗山圣母")));
    mapPropers.insert(std::make_pair(721,CellInfo(OPTIONAL,NOCOLOR,"Saint Apollinaris（主教、殉道）")));
    mapPropers.insert(std::make_pair(721,CellInfo(OPTIONAL,NOCOLOR,"圣老楞佐·布林希（司铎、圣师）")));
    mapPropers.insert(std::make_pair(722,CellInfo(MEMORIAL,WHITE,"圣玛利亚·玛达肋纳")));
    mapPropers.insert(std::make_pair(723,CellInfo(OPTIONAL,NOCOLOR,"圣妇彼济大（会士）")));
    mapPropers.insert(std::make_pair(725,CellInfo(FEAST,RED,"圣雅各伯（宗徒）")));
    mapPropers.insert(std::make_pair(726,CellInfo(MEMORIAL,WHITE,"圣若亚敬及圣亚纳（圣母双亲）")));
    mapPropers.insert(std::make_pair(729,CellInfo(MEMORIAL,WHITE,"圣玛尔大")));
    mapPropers.insert(std::make_pair(730,CellInfo(OPTIONAL,NOCOLOR,"圣伯多禄·金言（主教、圣师）")));
    mapPropers.insert(std::make_pair(731,CellInfo(OPTIONAL,NOCOLOR,"圣依纳爵·罗耀拉（司铎）")));
    
    //8月
    mapPropers.insert(std::make_pair(801,CellInfo(MEMORIAL,WHITE,"圣亚丰索（主教、圣师）")));
    mapPropers.insert(std::make_pair(802,CellInfo(OPTIONAL,NOCOLOR,"圣艾伯铎（司铎）")));
    mapPropers.insert(std::make_pair(802,CellInfo(OPTIONAL,NOCOLOR,"圣欧瑟伯（主教）")));
    mapPropers.insert(std::make_pair(804,CellInfo(MEMORIAL,WHITE,"圣若翰·维雅纳（司铎）")));
    mapPropers.insert(std::make_pair(805,CellInfo(OPTIONAL,NOCOLOR,"圣母大殿奉献日")));
    mapPropers.insert(std::make_pair(806,CellInfo(LORD,WHITE,"耶稣显圣容")));
    mapPropers.insert(std::make_pair(807,CellInfo(OPTIONAL,NOCOLOR,"圣嘉耶当（司铎）")));
    mapPropers.insert(std::make_pair(807,CellInfo(OPTIONAL,NOCOLOR,"圣思道二世（教宗）及同伴（殉道）")));
    mapPropers.insert(std::make_pair(808,CellInfo(MEMORIAL,WHITE,"圣道明（司铎）")));
    mapPropers.insert(std::make_pair(809,CellInfo(OPTIONAL,NOCOLOR,"St. Teresa Benedicta of the Cross（童贞、殉道）")));
    mapPropers.insert(std::make_pair(810,CellInfo(FEAST,RED,"圣老楞佐（执事、殉道）")));
    mapPropers.insert(std::make_pair(811,CellInfo(MEMORIAL,WHITE,"圣女佳兰")));
    mapPropers.insert(std::make_pair(812,CellInfo(OPTIONAL,NOCOLOR,"Saint Jane Frances de Chantal（会士）")));
    mapPropers.insert(std::make_pair(813,CellInfo(OPTIONAL,NOCOLOR,"圣彭谦（教宗、殉道）及圣希玻里（司铎、殉道）")));
    mapPropers.insert(std::make_pair(814,CellInfo(MEMORIAL,RED,"圣高比（司铎、殉道）")));
    mapPropers.insert(std::make_pair(815,CellInfo(SOLEMNITY,WHITE,"圣母蒙召升天节")));
    mapPropers.insert(std::make_pair(816,CellInfo(OPTIONAL,NOCOLOR,"匈牙利的圣斯德望国王")));
    mapPropers.insert(std::make_pair(819,CellInfo(OPTIONAL,NOCOLOR,"圣若望·欧德（司铎）")));
    mapPropers.insert(std::make_pair(820,CellInfo(MEMORIAL,WHITE,"圣伯尔纳铎（院长、圣师）")));
    mapPropers.insert(std::make_pair(821,CellInfo(MEMORIAL,WHITE,"圣庇护十世（教宗）")));
    mapPropers.insert(std::make_pair(822,CellInfo(MEMORIAL,WHITE,"圣母天后(圣母无玷圣心瞻礼)")));
    mapPropers.insert(std::make_pair(823,CellInfo(OPTIONAL,NOCOLOR,"圣罗撒")));
    mapPropers.insert(std::make_pair(824,CellInfo(FEAST,RED,"圣巴尔多禄茂（宗徒）")));
    mapPropers.insert(std::make_pair(825,CellInfo(OPTIONAL,NOCOLOR,"圣路易国王；或圣若瑟·加拉桑（司铎）")));
    mapPropers.insert(std::make_pair(827,CellInfo(MEMORIAL,WHITE,"圣莫尼加")));
    mapPropers.insert(std::make_pair(828,CellInfo(MEMORIAL,WHITE,"圣奥思定（主教、圣师）")));
    mapPropers.insert(std::make_pair(829,CellInfo(MEMORIAL,RED,"圣若翰洗者受难（殉道）")));
    
    //9月
    mapPropers.insert(std::make_pair(903,CellInfo(MEMORIAL,WHITE,"圣额我略一世（教宗、圣师）")));
    mapPropers.insert(std::make_pair(908,CellInfo(FEAST,WHITE,"圣母诞辰")));
    mapPropers.insert(std::make_pair(909,CellInfo(OPTIONAL,NOCOLOR,"圣伯多禄·高华（司铎）")));
    mapPropers.insert(std::make_pair(912,CellInfo(OPTIONAL,NOCOLOR,"圣母圣名节")));
    mapPropers.insert(std::make_pair(913,CellInfo(MEMORIAL,WHITE,"圣金口若望（主教、圣师）")));
    mapPropers.insert(std::make_pair(914,CellInfo(LORD,RED,"光荣十字圣架")));
    mapPropers.insert(std::make_pair(915,CellInfo(MEMORIAL,WHITE,"痛苦圣母")));
    mapPropers.insert(std::make_pair(916,CellInfo(MEMORIAL,RED,"圣高尔乃略（教宗、殉道）及圣西彼廉（主教、殉道）")));
    mapPropers.insert(std::make_pair(917,CellInfo(OPTIONAL,NOCOLOR,"圣罗伯·白敏（主教、圣师）")));
    mapPropers.insert(std::make_pair(919,CellInfo(OPTIONAL,NOCOLOR,"圣雅纳略（主教、殉道）")));
    mapPropers.insert(std::make_pair(920,CellInfo(MEMORIAL,RED,"圣金大建及同伴（殉道）")));
    mapPropers.insert(std::make_pair(921,CellInfo(FEAST,RED,"圣玛窦（宗徒、圣史）")));
    mapPropers.insert(std::make_pair(923,CellInfo(OPTIONAL,NOCOLOR,"圣比约神父（司铎）")));
    mapPropers.insert(std::make_pair(926,CellInfo(OPTIONAL,NOCOLOR,"圣葛斯及圣达弥盎（殉道）")));
    mapPropers.insert(std::make_pair(927,CellInfo(MEMORIAL,WHITE,"圣云先（司铎）")));
    mapPropers.insert(std::make_pair(928,CellInfo(OPTIONAL,NOCOLOR,"圣文策老（殉道）")));
    mapPropers.insert(std::make_pair(928,CellInfo(OPTIONAL,NOCOLOR,"圣老楞佐·卢斯及同伴（殉道）")));
    mapPropers.insert(std::make_pair(929,CellInfo(FEAST,WHITE,"圣弥额尔大天使、圣加俾额尔大天使、圣辣法尔大天使")));
    mapPropers.insert(std::make_pair(930,CellInfo(MEMORIAL,WHITE,"圣热罗尼莫（司铎、圣师）")));
    
    //10月
    mapPropers.insert(std::make_pair(1001,CellInfo(MEMORIAL,WHITE,"里修的圣德兰（小德兰）（童贞、圣师）")));
    mapPropers.insert(std::make_pair(1002,CellInfo(MEMORIAL,WHITE,"护守天使")));
    mapPropers.insert(std::make_pair(1004,CellInfo(MEMORIAL,WHITE,"亚西西的圣方济各")));
    mapPropers.insert(std::make_pair(1006,CellInfo(OPTIONAL,NOCOLOR,"圣博诺（司铎）")));
    mapPropers.insert(std::make_pair(1007,CellInfo(MEMORIAL,WHITE,"玫瑰圣母")));
    mapPropers.insert(std::make_pair(1009,CellInfo(OPTIONAL,NOCOLOR,"圣德尼及同伴（殉道）")));
    mapPropers.insert(std::make_pair(1009,CellInfo(OPTIONAL,NOCOLOR,"圣若望·良纳第（司铎）")));
    mapPropers.insert(std::make_pair(1011,CellInfo(MEMORIAL,WHITE,"圣若望廿三世（教宗）")));
    mapPropers.insert(std::make_pair(1014,CellInfo(OPTIONAL,NOCOLOR,"圣加理斯多一世（教宗、殉道）")));
    mapPropers.insert(std::make_pair(1015,CellInfo(MEMORIAL,WHITE,"亚维拉的圣德兰（大德兰）（童贞、圣师）")));
    mapPropers.insert(std::make_pair(1016,CellInfo(OPTIONAL,NOCOLOR,"圣玛加利大·亚拉高（童贞）")));
    mapPropers.insert(std::make_pair(1016,CellInfo(OPTIONAL,NOCOLOR,"圣赫德维（圣妇、会士）")));
    mapPropers.insert(std::make_pair(1017,CellInfo(MEMORIAL,RED,"圣依纳爵·安提约基亚（主教、殉道）")));
    mapPropers.insert(std::make_pair(1018,CellInfo(FEAST,RED,"圣路加（宗徒、圣史）")));
    mapPropers.insert(std::make_pair(1019,CellInfo(MEMORIAL,RED,"圣若望·贝巴（司铎）及圣依撒格·饶觉（司铎）及同伴（殉道）")));
    mapPropers.insert(std::make_pair(1020,CellInfo(OPTIONAL,NOCOLOR,"圣十字保禄瞻礼（司铎）")));
    mapPropers.insert(std::make_pair(1022,CellInfo(MEMORIAL,WHITE,"圣若望·保禄二世（教宗）")));
    mapPropers.insert(std::make_pair(1023,CellInfo(OPTIONAL,NOCOLOR,"圣若望·嘉庇当（司铎）")));
    mapPropers.insert(std::make_pair(1024,CellInfo(OPTIONAL,NOCOLOR,"圣安多尼·加烈)（主教）")));
    mapPropers.insert(std::make_pair(1028,CellInfo(FEAST,RED,"圣西满及圣犹达（宗徒）")));
    
    //11月
    mapPropers.insert(std::make_pair(1101,CellInfo(SOLEMNITY,WHITE,"诸圣节")));
    mapPropers.insert(std::make_pair(1102,CellInfo(LORD,WHITE,"追思亡者")));
    mapPropers.insert(std::make_pair(1103,CellInfo(OPTIONAL,NOCOLOR,"圣玛尔定·包瑞斯（修士）")));
    mapPropers.insert(std::make_pair(1104,CellInfo(MEMORIAL,WHITE,"圣嘉禄·鲍荣茂（主教）")));
    mapPropers.insert(std::make_pair(1109,CellInfo(LORD,WHITE,"祝圣拉特朗大殿")));
    mapPropers.insert(std::make_pair(1110,CellInfo(MEMORIAL,WHITE,"大圣良一世（教宗、圣师）")));
    mapPropers.insert(std::make_pair(1111,CellInfo(MEMORIAL,WHITE,"圣玛尔定（都尔主教）")));
    mapPropers.insert(std::make_pair(1112,CellInfo(MEMORIAL,RED,"圣若撒法（主教、殉道）")));
    mapPropers.insert(std::make_pair(1115,CellInfo(OPTIONAL,NOCOLOR,"圣亚尔伯（主教、圣师）")));
    mapPropers.insert(std::make_pair(1116,CellInfo(OPTIONAL,NOCOLOR,"苏格兰的圣玛加利大")));
    mapPropers.insert(std::make_pair(1116,CellInfo(OPTIONAL,NOCOLOR,"圣日多达（童贞）")));
    mapPropers.insert(std::make_pair(1117,CellInfo(MEMORIAL,WHITE,"匈牙利的圣依撒伯尔（会士）")));
    mapPropers.insert(std::make_pair(1118,CellInfo(OPTIONAL,NOCOLOR,"圣伯多禄及圣保禄大殿奉献日")));
    mapPropers.insert(std::make_pair(1121,CellInfo(MEMORIAL,WHITE,"献圣母于主堂")));
    mapPropers.insert(std::make_pair(1122,CellInfo(MEMORIAL,RED,"圣则济利亚（童贞、殉道）")));
    mapPropers.insert(std::make_pair(1123,CellInfo(OPTIONAL,NOCOLOR,"圣克来孟一世（教宗、殉道）")));
    mapPropers.insert(std::make_pair(1123,CellInfo(OPTIONAL,NOCOLOR,"圣高隆邦（院长）")));
    mapPropers.insert(std::make_pair(1124,CellInfo(MEMORIAL,RED,"圣陈安勇乐（司铎）及同伴（殉道）")));
    mapPropers.insert(std::make_pair(1125,CellInfo(OPTIONAL,NOCOLOR,"亚历山大的圣凯瑟琳（童贞、殉道）")));
    mapPropers.insert(std::make_pair(1130,CellInfo(FEAST,RED,"圣安德肋（宗徒）")));
    
    //12月
    mapPropers.insert(std::make_pair(1203,CellInfo(MEMORIAL,WHITE,"圣方济沙勿略（司铎）")));
    mapPropers.insert(std::make_pair(1204,CellInfo(OPTIONAL,NOCOLOR,"圣若望·达玛森（司铎、圣师）")));
    mapPropers.insert(std::make_pair(1206,CellInfo(OPTIONAL,NOCOLOR,"圣尼各老（主教）")));
    mapPropers.insert(std::make_pair(1207,CellInfo(MEMORIAL,WHITE,"圣安博（主教、圣师）")));
    mapPropers.insert(std::make_pair(1208,CellInfo(SOLEMNITY,WHITE,"圣母无玷始胎")));
    mapPropers.insert(std::make_pair(1209,CellInfo(OPTIONAL,NOCOLOR,"圣若望·迭戈")));
    mapPropers.insert(std::make_pair(1210,CellInfo(MEMORIAL,WHITE,"洛雷托圣母")));
    mapPropers.insert(std::make_pair(1211,CellInfo(OPTIONAL,NOCOLOR,"圣达玛稣一世（教宗）")));
    mapPropers.insert(std::make_pair(1212,CellInfo(FEAST,WHITE,"瓜达卢佩圣母")));
    mapPropers.insert(std::make_pair(1213,CellInfo(MEMORIAL,RED,"圣路济亚（贞女、殉道）")));
    mapPropers.insert(std::make_pair(1214,CellInfo(MEMORIAL,WHITE,"圣十字若望（司铎、圣师）")));
    mapPropers.insert(std::make_pair(1221,CellInfo(OPTIONAL,NOCOLOR,"圣加尼修（司铎、圣师）")));
    mapPropers.insert(std::make_pair(1223,CellInfo(OPTIONAL,NOCOLOR,"圣若望·甘迪")));
    mapPropers.insert(std::make_pair(1225,CellInfo(SOLEMNITY,WHITE,"吾主诞生日（圣诞节）")));
    mapPropers.insert(std::make_pair(1226,CellInfo(FEAST,RED,"圣斯德望（首位殉道）")));
    mapPropers.insert(std::make_pair(1227,CellInfo(FEAST,WHITE,"圣若望（宗徒、圣史）")));
    mapPropers.insert(std::make_pair(1228,CellInfo(FEAST,RED,"诸圣婴孩")));
    mapPropers.insert(std::make_pair(1229,CellInfo(OPTIONAL,NOCOLOR,"圣多玛斯·百克（主教、殉道）")));
    mapPropers.insert(std::make_pair(1231,CellInfo(OPTIONAL,NOCOLOR,"圣西物斯德一世（教宗）")));
}


LiturgicYear::LiturgicYear(const int& y)
	: year(y)
{
	init();
}

LiturgicYear::~LiturgicYear(void)
{
}

LiturgicDay LiturgicYear::getLiturgicDay(const Date& d)
{
	if(d.year() != year)
		return LiturgicDay();

    //填充数据
    LiturgicDay ld = d;
	if(ld<bl)
	{
		testChristmas1(ld);
	}
	else if(ld<aw)
	{
		testOrdinary1(ld);
	}
	else if(ld<easter)
	{
		testLent(ld);
	}
	else if(ld<=ps)
	{
		testEaster(ld);
	}
	else if(ld<av)
	{
		testOrdinary2(ld);
	}
	else if(ld<cm)
	{
		testAdvent(ld);
	}
	else
	{
		testChristmas2(ld);
	}

	testProper(ld);

    return ld;
}

std::string LiturgicYear::toString() const
{
    std::ostringstream ostr;
    ostr<<"年份:"<<year<<std::endl;
	ostr<<"主显节\t\t:\t"<<ep.toString()<<std::endl;
	ostr<<"主受洗日\t\t:\t"<<bl.toString()<<std::endl;
	ostr<<"圣灰礼仪\t\t:\t"<<aw.toString()<<std::endl;
	ostr<<"复活节\t\t:\t"<<easter.toString()<<std::endl;
    ostr<<"圣神降临\t\t:\t"<<ps.toString()<<std::endl;
    ostr<<"将临期第一主日\t:\t"<<av.toString()<<std::endl;
    
    return ostr.str();
}

void LiturgicYear::init()
{
	//主显节
	LiturgicDay t(year,1,2);
	ep = t.addDays((7-t.dayOfWeek())%7);

	//主受洗日(圣诞期的结束)
	if(ep.day()>6)
	{
		bl = ep.addDays(1);
	}
	else
	{
		bl = ep.addDays(7);
	}

	//复活节
	{
		int y,c,n,k,i,j,l,m,d;
		y = year;

		c = y/100;
		n = y - 19*(y/19);
		k = (c - 17)/25;
		i = c - c/4 - (c-k)/3 + 19*n + 15;
		i = i - 30*(i/30);
		i = i - (i/28) * (1 - (i/28) * (29/(i+1)) * ((21 - n)/11));
		j = y + y/4 + i + 2 - c + c/4;
		j = j - 7*(j/7);
		l = i - j;

		m = 3 + (l+40)/44;
		d = l + 28 - 31*(m/4);

		easter.setDate(year, m, d);
	}

	//圣灰礼仪（周三 四旬期开始）
	aw = easter.addDays(-46);

	//圣神降临节瞻礼
	ps = easter.addDays(49);

	t.setDate(year,11,30);
	//将临期第一主日
	av = t.addDays(0-t.dayOfWeek());

	//圣诞节
	cm.setDate(year,12,25);
}

void LiturgicYear::testChristmas1(LiturgicDay& ld)
{
	if((!ld.isValid()) || ld<LiturgicDay(year,1,1) || ld>bl)
		return;

	ld.setSeason(CHRISTMAS);

	Date lastChristmas(year-1,12,25);
	int dayFromLC = lastChristmas.daysTo(ld);
	ld.setWeekOfSeason((dayFromLC-lastChristmas.dayOfWeek()-1)/7 + 1);
	if(ld.dayOfWeek()==SUN)
	{
		ld.appendCell(SUNDAY,WHITE,ld.toWeekdayString());
	}
	else
	{
		ld.appendCell(WEEKDAY,WHITE,ld.toWeekdayString());
	}

	if(ld<ep)
	{

	}
	else if(ld==ep)
	{
		ld.appendCell(SOLEMNITY,WHITE,"主显节");
	}
	else if(ld<bl)
	{

	}
}

void LiturgicYear::testOrdinary1(LiturgicDay& ld)
{
	if((!ld.isValid()) || ld<bl || ld>=aw)
		return;

	ld.setSeason(ORDINARY);
	int weekBegin = aw.weekNumber();
	ld.setWeekOfSeason( ld.weekNumber()-bl.weekNumber()+1 );
	if(ld.dayOfWeek()==SUN)
	{
		ld.appendCell(SUNDAY,GREEN,ld.toWeekdayString());
	}
	else
	{
		ld.appendCell(WEEKDAY,GREEN,ld.toWeekdayString());
	}


	if(ld == bl)
	{
		ld.appendCell(LORD,WHITE,"主受洗日");
	}

}

void LiturgicYear::testLent(LiturgicDay& ld)
{
	if((!ld.isValid()) || ld<aw || ld>=easter)
		return;

	ld.setSeason(LENT);
	ld.setWeekOfSeason( ld.weekNumber()-aw.weekNumber() );
	if(ld.dayOfWeek()==SUN)
	{
		if(ld.getWeekOfSeason() == 4)
			ld.appendCell(SUNDAY,ROSE,ld.toWeekdayString());
		else
			ld.appendCell(SUNDAY,PURPLE,ld.toWeekdayString());
	}
	else
	{
		ld.appendCell(WEEKDAY,PURPLE,ld.toWeekdayString());
	}

	if(ld == aw)
	{
		ld.appendCell(ASHWED,PURPLE,"圣灰礼仪");
	}
	else if(ld.getWeekOfSeason() == 6)
	{
		//圣周
		switch(ld.dayOfWeek())
		{
		case 0:
			ld.appendCell(SUNDAY,RED,"圣枝主日(基督苦难主日)");
			break;
		case 1:
			ld.appendCell(HOLYWEEK,PURPLE,"圣周一");
			break;
		case 2:
			ld.appendCell(HOLYWEEK,PURPLE,"圣周二");
			break;
		case 3:
			ld.appendCell(HOLYWEEK,PURPLE,"圣周三");
			break;
		case 4:
			ld.appendCell(TRIDUUM,WHITE,"主的晚餐");
			break;
		case 5:
			ld.appendCell(TRIDUUM,RED,"耶稣受难");
			break;
		case 6:
			ld.appendCell(TRIDUUM,WHITE,"基督安眠墓中(复活节前夕)");
			break;
		}
	}
}

void LiturgicYear::testEaster(LiturgicDay& ld)
{
	if((!ld.isValid()) || ld<easter || ld>ps)
		return;

	ld.setSeason(EASTER);
	ld.setWeekOfSeason(ld.weekNumber()-easter.weekNumber()+1);

	int dayFromEaster = easter.daysTo(ld);

	if(dayFromEaster>7)
	{
		if(ld.dayOfWeek()==SUN)
		{
			ld.appendCell(SUNDAY,WHITE,ld.toWeekdayString());
		}
		else
		{
			ld.appendCell(WEEKDAY,WHITE,ld.toWeekdayString());
		}
	}

	if(dayFromEaster<8)
	{
		if(dayFromEaster == 0)
		{
			ld.appendCell(SOLEMNITY,WHITE,"复活节");
		}
		else if(dayFromEaster < 7)
		{
			std::ostringstream ostr;
			ostr<<"复活节庆期第"<<getChineseNumStr(ld.dayOfWeek()+1)<<"日";

			ld.appendCell(SOLEMNITY,WHITE,ostr.str());
		}
		else
		{
			ld.appendCell(SOLEMNITY,WHITE,ld.toWeekdayString());
		}
	}
	else if(dayFromEaster == 39)
	{
		ld.appendCell(SOLEMNITY,WHITE,"耶稣升天");
	}
	else if(dayFromEaster == 49)
	{
		ld.appendCell(SOLEMNITY,RED,"圣神降临节");
	}
}

void LiturgicYear::testOrdinary2(LiturgicDay& ld)
{
	if((!ld.isValid()) || ld<=ps || ld>=av)
		return;

	int weekEnd = av.weekNumber() - 1;		//将临期第一主日为常年期的结束
	ld.setWeekOfSeason( 34-(weekEnd-ld.weekNumber()) );
	if(ld.dayOfWeek()==SUN)
	{
		ld.appendCell(SUNDAY,GREEN,ld.toWeekdayString());
		if(ld.getWeekOfSeason() == 34)
		{
			ld.appendCell(SOLEMNITY,WHITE,"基督普世君王节");
		}
	}
	else
	{
		ld.appendCell(WEEKDAY,GREEN,ld.toWeekdayString());
	}



	int dayFromEaster = easter.daysTo(ld);

	if(dayFromEaster == 56)
	{
		ld.appendCell(SOLEMNITY,WHITE,"圣三主日");
	}
	else if(dayFromEaster == 60)
	{
		//可移至主日庆祝 + 63
		ld.appendCell(SOLEMNITY,WHITE,"基督圣体圣血节");
	}
	else if(dayFromEaster == 68)
	{
		ld.appendCell(SOLEMNITY,WHITE,"耶稣圣心节");
	}
	else if(dayFromEaster == 69)
	{
		ld.appendCell(MEMORIAL,WHITE,"圣母无玷圣心");
	}

	ld.setSeason(ORDINARY);
}

void LiturgicYear::testAdvent(LiturgicDay& ld)
{
	if((!ld.isValid()) || ld<av || ld>=cm)
		return;

	ld.setSeason(ADVENT);
	ld.setWeekOfSeason(ld.weekNumber()-av.weekNumber()+1);
	if(ld.dayOfWeek() == SUN)
	{
		if(ld.getWeekOfSeason() == 3)
		{
			ld.appendCell(SUNDAY,ROSE,ld.toWeekdayString());
		}
		else
		{
			ld.appendCell(SUNDAY,PURPLE,ld.toWeekdayString());
		}
	}
	else
	{
		ld.appendCell(SUNDAY,PURPLE,ld.toWeekdayString());
	}
}

void LiturgicYear::testChristmas2(LiturgicDay& ld)
{
	if((!ld.isValid()) || ld<cm || ld>LiturgicDay(year,12,31))
		return;

	ld.setSeason(CHRISTMAS);
	if(cm.dayOfWeek() == SUN && ld==Date(year,12,31))
	{
		ld.setWeekOfSeason(2);
		ld.appendCell(SUNDAY,WHITE,ld.toWeekdayString());
	}
	else
	{
		ld.setWeekOfSeason(1);
		if(ld==Date(year,12,31))
		{
			ld.appendCell(WEEKDAY,WHITE,ld.toWeekdayString());
		}
	}

	switch(cm.daysTo(ld))
	{
	case 1:
	case 2:
	case 3:
	case 4:
	case 5:
	case 6:
		{
			std::ostringstream ostr;
			if(ld.dayOfWeek() == SUN)
			{
				ostr<<"圣诞节庆期第"<<getChineseNumStr(cm.daysTo(ld)+1)<<"日";
				ld.appendCell(SUNDAY,WHITE,ostr.str());
			}
			else
			{
				ostr<<"圣诞节庆期第"<<getChineseNumStr(cm.daysTo(ld)+1)<<"日";
				ld.appendCell(WEEKDAY,WHITE,ostr.str());
			}
		}
		break;
	}

	//圣家节
	if(cm.dayOfWeek() == SUN)
	{
		if(ld == Date(year,12,30))
			ld.appendCell(LORD,WHITE,"圣家节");
	}
	else
	{
		if(ld.dayOfWeek() == SUN)
			ld.appendCell(LORD,WHITE,"圣家节");
	}
}

void LiturgicYear::testProper(LiturgicDay& ld)
{
	int v = ld.month()*100+ld.day();

	std::multimap<int,CellInfo>::const_iterator iter = mapPropers.find(v);
	std::multimap<int,CellInfo>::size_type count = mapPropers.count(v);

	std::multimap<int,CellInfo>::size_type i = 0;
	while(i<count)
	{
		ld.appendCell(iter->second);

		++iter;
		++i;
	}

}
