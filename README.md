**Caso 4**

Basándonos en los resultados se obtuvo para los APIs:

1. Get35 
   Obtiene un 35% de los registros en la tabla
    * Tiempo promedio de respuesta: 4044ms
    * Rango de tiempo de respuesta: 1339-11704ms
    * CPU: 3.2%
    * Memoria: 27.9MiB
    * Conexiones: 2

3. Get35FromPool
   Obtiene un 35% de los registros y tiene un fixed size connection pool
    * Tiempo promedio de respuesta: 1129ms
    * Rango de tiempo de respuesta: 279-8329ms
    * CPU: 3.3%
    * Memoria: 27.9MiB
    * Conexiones: 2

2. Get35WithCache
   Obtiene un 35% de los registros con cache por medio de un redis server
    * Tiempo promedio de respuesta: 68ms
    * Rango de tiempo de respuesta: 40-92ms
    * CPU: 3.4% 
    * Memoria: 28MiB
    * Conexiones: 2


*Conclusiones*
1. Impacto del Pool de Conexiones:
   La reducción de conexiones fluctuantes y al poseer una cantidad estable de conexiones se llega a mostrar una mejora notable en la eficiencia     de los recursos que forman parte de la base de datos. Los endpoints se encargan de gestionar mejor las conexiones debido al uso del pool

2. Eficiencia del Caché:
   La incorporación de Redis como sistema de caching ayudó a reducir notablemente los tiempos de respuesta, especialmente en endpoints como         Get35WithCache con tiempos veloces comparado a otros endpoints que dependen más de la base de datos.
   Redis, al no consumir muchos recursos de la CPU y la memoria, juega un papel importante en la reducción de tiempo de respuesta.

3. Rendimiento general del Sistema:
   El sistema en su conjunto utiliza de manera eficiente la CPU y la memoria. La optimización del backend API y la incorporación de Redis demostraron un impacto positivo tanto al gestionar los recursos como al tomar en cuenta la respuesta de los endpoints.
   A pesar de que los tiempos de respuesta varían significativamente, se observa una clara tendencia de mejora en los endpoints que parovechan la caché. Las configuraciones de pool y caché deben mantenerse y optimizarse seguido para obtener un rendimiento óptimo.
   

*Evidencia*

Antes:
![Antes](https://github.com/user-attachments/assets/bed62de7-b796-4ab2-9d04-5dbd22e74fbb)

Durante:
![Durante](https://github.com/user-attachments/assets/cf61625b-0c7d-483b-80a6-adcc48581fb5)

Resultados:
![Resultados](https://github.com/user-attachments/assets/8fa222d5-dc60-48a8-9d45-51160aff09e9)

Estadísticas Redis:
![statRedis](https://github.com/user-attachments/assets/c79baf23-7fba-4002-ade4-a65d576cc92b)

Contenedores Docker:
![Containers](https://github.com/user-attachments/assets/9648e1b7-ee90-4c16-939c-0c45e0164a28)


