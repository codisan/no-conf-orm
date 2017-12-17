# 无配置ORM框架

![alt text](https://github.com/codisan/no-conf-orm/blob/master/doc/imgs/index.jpg "lol...")

## 定义model
``` java
@Table(name="user")
public class User extends Dao {
	@Column(name = "user_id", primaryKey=true, autoIncrement=true)
	public Integer userId;
	
	@Column(name = "email_id")
	public String emailId;
	
	public String password;
	
	@Column(name = "last_name")
	public String lastName;
	
	@Column(name = "first_name")
	public String firstName;
	
	@Override
	public String toString() {
		return "User [userId=" + userId + ", emailId=" + emailId + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + "]";
	}	
}
```

## 单对象查询
``` java
User u = Dao.findOne(User.class, "where user_id = 2");
System.out.println(u);
```

## 新增
``` java
User u = new User();
u.firstName = "xx";
u.lastName = "oo";
u.emailId = "xx@oo.com";
u.password = "xx pass word";
		
int row = Dao.insert(User.class, u);
```

## 更新
``` java
User u = Dao.findOne(User.class, "where user_id = 2");
u.firstName = "333333333333";
u.update(User.class, u);
```

## 删除
``` java
User u = Dao.findOne(User.class, "where user_id = 6");
u.destroy(User.class, u);
```

