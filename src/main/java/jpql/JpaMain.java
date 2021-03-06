package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.changeTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.changeTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.changeTeam(teamB);
            em.persist(member3);

/*
            em.flush();
            em.clear();
*/

/*
            //타입 쿼리와 논타입 쿼리
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            Query query3 = em.createQuery("select m.username, m.age from Member m");
*/

/*
            //getSingleResult에서 결과가 없을 때, 결과가 두 개 이상일 때 예외 발생
            Member result = query.getSingleResult();
            System.out.println("result = " + result);
*/

/*
            //파라미터 바인딩
            Member result = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "정대만")
                    .getSingleResult();
            System.out.println("result = " + result.getUsername());
*/

/*
            //엔티티 프로젝션 (조회한 데이터는 영속성 컨텍스트에서 관리된다.)
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            Member findMember = result.get(0);
            findMember.setAge(20);

            //성능 문제가 있을 수 있기 때문에 조인으로 명확하게 나타내야 한다. (예측할 수 있게 해야 함)
//            List<Team> result = em.createQuery("select m.team from Member m", Team.class)
//                    .getResultList();
            List<Team> result = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();

            //임베디드 타입 프로젝션
            em.createQuery("select o.address from Order o", Address.class).getResultList();

            //스칼라 타입 프로젝션
            //방법 1 (Query 이용, Object를 Object[]로 변환하여 값 조회 가능)
            List resultList1 = em.createQuery("select m.username, m.age from Member m").getResultList();
            for (Object o : resultList1) {
                Object[] result = (Object[]) o;
                System.out.println("username = " + result[0]);
                System.out.println("age = " + result[1]);
            }

            //방법 2 (TypedQuery 이용)
            List<Object[]> resultList2 = em.createQuery("select m.username, m.age from Member m").getResultList();
            Object[] result1 = resultList2.get(0);
            System.out.println("username = " + result1[0]);
            System.out.println("age = " + result1[1]);

            //방법 3 (new 명령어와 DTO 이용)
            List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();
            MemberDTO memberDTO = result.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());
*/

/*
            //페이징 처리
            String query = "select m from Member m order by m.age desc";
            List<Member> result = em.createQuery(query, Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();
*/
            //내부 조인 (inner 생략 가능)
//            String query = "select m from Member m join m.team t";
            
            //외부 조인 (outer 생략 가능)
//            String query = "select m from Member m left join m.team t";

            //세타 조인 (join 자체를 생략 가능)
//            String query = "select m from Member m, Team t where m.username = t.name";

            //조인 대상 필터링
//            String query = "select m from Member m left join m.team t on t.name = 'teamA'";

            //연관 관계가 없는 외부 조인
//            String query = "select m from Member m left join Team t on m.username = t.name";

            //스칼라 서브쿼리 (JPA x, Hibernate o) -> 서브쿼리는 where, having절에서만 사용 가능
//            String query = "select (select avg(m1.age) from Member m1) as avgAge from Member m";

/*
            //JPQL 타입 표현
            String query = "select m.username, TRUE, 'HELLO' from Member m " +
                            "where m.type = :userType";

            List<Object[]> resultList = em.createQuery(query)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

            for (Object[] o : resultList) {
                System.out.println("o[0] = " + o[0]);
                System.out.println("o[0] = " + o[1]);
                System.out.println("o[0] = " + o[2]);
            }
*/

/*
            //조건식 (case 식)
            String query =
                    "select " +
                        "case when m.age <= 10 then '학생요금' " +
                        "     when m.age >= 60 then '경로요금' " +
                        "     else '일반요금' " +
                        "end " +
                    "from Member m";

            List<String> result = em.createQuery(query, String.class).getResultList();
            System.out.println("result.get(0) = " + result.get(0));
*/

/*
            //조회 후 값이 없으면 설정한 값 반환
            String query = "select coalesce(m.username, '이름 없는 회원') as username from Member m ";

            //두 값이 같으면 null, 아니면 첫 번째 값 반환
            String query = "select nullif(m.username, '관리자') from Member m";

*/

/*
            //문자열 합치기
//            String query = "select concat('a', 'b') from Member m";
            // 문자열 자르기 (원본, 시작 위치, 가져올 문자 수)
//            String query = "select substring(m.username, 2, 3) from Member m";
            //문자열 위치 찾기
//            String query = "select locate('de', 'abcdefg') from Member m";
            //컬렉션 크기 반환
//            String query = "select size(t.members) from Team t";
            //리스트 타입의 컬렉션에서 위치 값을 구할 때 사용
//            String query = "select index(t.members) from Team t";

            List<Integer> result = em.createQuery(query, Integer.class).getResultList();

            for (Integer s : result) {
                System.out.println("s = " + s);
            }
*/

            //사용자 정의 함수 사용 (group_concat은 결과를 한 줄로 출력하는 함수)
//            String query = "select function('group_concat', m.username) from Member m";
//            String query = "select group_concat(m.username) from Member m"; //Hibernate 지원 (inject language or reference)

            //경로 탐색
//            String query = "select m.username from Member m"; //상태 필드 (탐색 X)
//            String query = "select m.team from Member m"; //단일 값 연관 경로 (묵시적 조인, 탐색 O)
//            String query = "select t.members.size from Team t"; //컬렉션 값 연관 경로 (묵시적 조인, 탐색 X)
//            String query = "select m.username from Team t join t.members m"; //컬렉션 값 연관 경로 (명시적 조인을 통해 탐색 가능)

            //fetch join
//            String query = "select m from Member m"; //N + 1 문제 발생 위험 (컬렉션 조회 시 문제점)
//            String query = "select m from Member m join fetch m.team"; //연관관계를 가진 데이터를 한 번에 조회 (다대일)

            //엔티티 직접 사용
//            String query = "select m from Member m where m = :member"; //member 기본키로 조회
//            String query = "select m from Member m where m.team = :team"; //member 외래키로 조회 (@JoinColumn에 설정된 값)

            //Named Query (캐시 기능, 쿼리 검증 기능)
/*
            List<Member> result = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();
*/

            //벌크 연산
            //flush 자동 호출 (query 발생 시 flush 후에 나가게 된다.)
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            System.out.println("resultCount = " + resultCount);

            em.clear();

            Member findMember = em.find(Member.class, member1.getId());

            //영속성 컨텍스트에 있는 값을 조회 (업데이트 반영 안됨) -> 새로 초기화 또는 미리 벌크연산 진행
            System.out.println("member1.age = " + findMember.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
