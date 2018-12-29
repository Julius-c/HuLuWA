# Final Project @author CC
- 葫芦娃大战妖精，系JAVA课程大实验。

- 葫芦娃与蝎子精带领的喽啰们在草地上自动战斗直到一方全部阵亡，爷爷与蛇精在阵地后排观战。

![效果图](C:\Users\CC\Desktop\pic.JPG)
## 项目概述
1. **项目构建**

  `mvn clean test package`即可得到`/target/calabash-1.0-SNAPSHOT.jar`可执行文件。

2. **使用说明**

  - 双击可执行文件后即出现游戏默认初始界面，葫芦娃以長蛇阵型出站，妖精们以鶴翼阵型迎战。
  - 点击`start`按钮后战斗开始，开始计时。敌对双方寻找敌人厮杀，相互接触后随即判定其中一方的死亡(该概率由双方初始人数比确定)，死亡后化作墓碑实体。游戏开始`6s`后每隔`3s`随机选择消失一个墓碑，以防战场堵塞。
  - 战斗结束后战斗过程已被记录在`/target/NEWLOG.log`文件中，可以点击`Load`按钮加载回放该场战斗，若觉得精彩可以重命名后加以保存，新一轮战斗会覆盖`NEWLOG.log`文件。
  - 战斗结束或回放结束后，可变换妖精们的阵型重新战斗。共有八种阵型可供选择，判定胜负的概率也随阵型变化而变化。
  - 项目根目录中`Demo`文件夹已事先存储部分阵型下的一场精彩站斗。

3. **实现细节**

- 代码结构说明
- 