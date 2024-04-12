## ABOUT
**Rig-bot** – telegram bot for rig monitoring (pool.binance.com).
It solves the problem of tracking the status of a farm connected to the binance mining pool using the telegram messenger.
<br><br>


## KEY FEATURES
The key features of the bot are:
- interval survey of rig status, saving and analyzing hashrates;
- notification of an authorized user about a problem (offline farm, power drop);
- clearing the farm status table every 24 hours;
- providing a 24-hour farm productivity graph;
- providing the average hashrate per hour as a string;
- providing a history for a period of time with a specified interval as a string;
- working with the bot using login and password.
<br><br>


## QUICK START
### Configuration:
1. Create a **"config"** folder in the same directory as the bot.
2. Create the configuration file **"application.properties"** in the "config" folder.
3. In the created file, specify the following settings, be sure to specify the pool address, bot name and token:
```
   rig.data.url=poolAddress
   telegram.bot.name=botName
   telegram.bot.token=botToken
```
4. In the configuration file **0-initiallog.xml** (src/main/resources/db/changelog/) in the changeSet with the id "insert_into_users " specify users with the desired username, password and role:
```
    <changeSet id="insert_into_users" author="author">
        <insert tableName="users">
            <column name="login" value="user_login"/>
            <column name="password" value="user_password"/>
            <column name="role_name" value="0"/>
        </insert>

        <insert tableName="users">
            <column name="login" value="admin_login"/>
            <column name="password" value="admin_password"/>
            <column name="role_name" value="1"/>
        </insert>
    </changeSet>
```
### Getting Started:
1. Build a project using maven.
2. Put the compiled project (rig-bot-1.3.3.jar) in the directory where the configuration folder (config) is located.
3. Install Java >= 17.
4. Launch the bot using the command line and the command "java –jar rig-bot-1.3.3.jar".
<br><br>


## USED TECHNOLOGIES <br>
### Previously used technologies:
- spring-boot-starter
- hibernate-core
- spring-boot-starter-data-jpa


### New technologies specifically for this project:
- telegram bot api
- h2
- logback + slf4j
- liquibase
- lombok
