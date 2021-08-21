package com.spring.tx;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务的隔离级别
 * 数据事务并发问题：
 * 假如现在有两个事务：Transaction1和Transaction2并发执行。
 * 1，脏读
 *      1）Transaction1将某条记录的AGE值从20修改为30
 *      2）Transaction2读取了Transaction1更新后的值：30
 *      3）Transaction1回滚，AGE值回复到了20
 *      4）Transaction2读取到的30就是一个无效的值
 *
 * 2，不可重复度（同一个事务期间）
 *      1）Transaction1读取AGE值为20
 *      2）Transaction2将AGE修改为30
 *      3）Transaction1再次读取AGE值为30，和第一次读取不一致。
 *
 * 3，幻读
 *      1）Transaction1读取了STUDENT表中的一部分数据
 *      2）Transaction2向STUDENT表中插入了新的行。
 *      3）Transaction1读取了STUDENT表时，多出了一些行。
 *
 *
 * 隔离级别
 * 数据库系统必须具有隔离并发运行各个事务的能力，使它们不会相互影响，避免各中并发问题。一个事务与其他事务隔离的程度称为隔离级别。
 * SQL中规定了多种事务隔离级别，不同的隔离级别对应不同的干扰程度，隔离级别越高，数据一致性就好，但并发性越弱。
 *      1）读为提交：READ UNCOMMENTED
 *          允许Transaction1读取Transaction2未提交的修改
 *      2）读已提交：READ COMMENTED
 *          要求Transaction1只能读取Transaction2以提交的修改。（解决脏读）
 *      3）可重复读：REPEATABLE READ
 *          确保Transaction1可以多次从一个字段中读取到相同的值，即Transaction1执行期间禁止其它事务对这个字段进行跟新。（解决不可重复读问题，mysql也会解决幻读问题，Oracle不会解决幻读问题）
 *      4）串行化：SERIALIZABLE
 *          确保Transaction1可以多次从一个表中读取到相同的行，在Transaction1执行期间，禁止其它事务对表进行增，删，改操作。
 */


@Service
public class BookService {

    @Autowired
    private BookDao bookDao;
    @Autowired
    private UserDao userDao;

    /**
     * 运行时异常默认回滚，编译时异常不会回滚
     *  事务参数：
     *
     */
//    @Transactional(timeout = 3)       //超时属性设置：事务超出指定时长，回滚事务
//    @Transactional(readOnly = true)     //设置事务为只读事务，加快查询操作不用管事务操作,默认false
//    @Transactional(rollbackFor = {ArithmeticException.class})    //那些异常发生回滚
//    @Transactional(rollbackForClassName = {"java.lang.ArithmeticException"})
    @Transactional(noRollbackForClassName = {"java.lang.ArithmeticException"})
//    @Transactional(noRollbackFor = {ArithmeticException.class})     //指定发生那些异常不回滚
    public void buyBook(Integer userId, Integer bookId){

        Integer stock = bookDao.getStock(bookId);
        if(stock != null){
            if(stock > 0){
                Integer price = bookDao.getPrice(bookId);
                //减库存
                int bookStatu = bookDao.updateStock(bookId);
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                int a = 10/0;
                //减账户余额
                int userStatu = userDao.updateBalance(userId, price);
            }else {
                throw new RuntimeException("库存量不足，无法购买！");
            }
        }else{
            throw new RuntimeException("用户不存在！");
        }
    }


    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)         //isolation调整事务的隔离级别
    public void readBookPrice(Integer bookId){
        Integer bookPrice = bookDao.getPrice(bookId);
        System.out.println(bookPrice);
    }


}
