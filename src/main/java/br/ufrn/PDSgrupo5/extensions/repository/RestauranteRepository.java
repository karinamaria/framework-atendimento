package br.ufrn.PDSgrupo5.extensions.repository;

import br.ufrn.PDSgrupo5.extensions.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
	@Query(value="SELECT r FROM Restaurante r WHERE r.pessoa.nome=?1")
    Restaurante buscarRestaurantePorNome(String nome);
}
