start :- 	thread_create(ID1, has_child(bob,X)), 
			mutex_lock(ÕmutexÕ),
			thread_create(ID2, read_child(ID1,X)), 
			loop(1,5,1, ID1), mutex_unlock(ÕmutexÕ).
			
read_child(ID, X) :- mutex_lock(ÕmutexÕ), 
					 thread_read(ID, X),
					 mutex_unlock(ÕmutexÕ).

has_child(bob, alex). 
has_child(bob, anna). 
has_child(bob, mary).

loop(I, To, Inc, ThreadId) :- Inc >= 0, I > To, !. 
loop(I, To, Inc, ThreadId) :- Inc < 0, I < To, !. 
loop(I, To, Inc, ThreadId) :- thread_has_next(ThreadId), 
								!, 
								thread_next_sol(ThreadId), 
								Next is I+Inc,
								loop(Next, To, Inc, ThreadId).
loop(I, To, Inc, ThreadId).
