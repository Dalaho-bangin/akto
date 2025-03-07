package com.akto.threat.detection.tasks;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class CleanupTask implements Task {

  private final SessionFactory sessionFactory;

  public CleanupTask(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void run() {
    ScheduledExecutorService cronExecutorService = Executors.newScheduledThreadPool(1);
    cronExecutorService.scheduleAtFixedRate(this::cleanup, 5, 30, TimeUnit.MINUTES);
  }

  private void cleanup() {
    try (Session session = this.sessionFactory.openSession()) {
      Transaction txn = session.beginTransaction();
      int deletedCount =
          session
              .createQuery("delete from MaliciousEventEntity m where m.createdAt < :startDate")
              .setParameter("startDate", LocalDateTime.now(ZoneOffset.UTC).minusDays(7))
              .executeUpdate();

      txn.commit();
      System.out.println("Number of rows deleted: " + deletedCount);
    }
  }
}
